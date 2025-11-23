package com.backend.dosol.service;

import com.backend.dosol.dto.youtube.YouTubeSearchResponse;
import com.backend.dosol.entity.Song;
import com.backend.dosol.repository.SongRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class YouTubeService {

	@Value("${youtube.api.key}")
	private String apiKey;

	@Value("${youtube.api.base-url}")
	private String baseUrl;

	private final WebClient webClient = WebClient.builder().build();
	private final SongRepository songRepository;

	public YouTubeSearchResponse searchVideo(String title, String artist) {
		String query = String.format("%s %s", title, artist);

		log.info("YouTube 검색 시작: {}", query);

		try {
			JsonNode response = webClient.get()
				.uri(uriBuilder -> uriBuilder
					.scheme("https")
					.host("www.googleapis.com")
					.path("/youtube/v3/search")
					.queryParam("part", "snippet")
					.queryParam("q", query)
					.queryParam("key", apiKey)
					.queryParam("type", "video")
					.queryParam("maxResults", 1)
					.build())
				.retrieve()
				.bodyToMono(JsonNode.class)
				.block();

			if (response != null && response.has("items") && response.get("items").size() > 0) {
				JsonNode firstItem = response.get("items").get(0);
				String videoId = firstItem.get("id").get("videoId").asText();
				String videoTitle = firstItem.get("snippet").get("title").asText();
				String channelTitle = firstItem.get("snippet").get("channelTitle").asText();
				String thumbnailUrl = firstItem.get("snippet").get("thumbnails").get("default")
					.get("url").asText();

				log.info("YouTube 검색 성공: videoId={}", videoId);

				return YouTubeSearchResponse.builder()
					.videoId(videoId)
					.title(videoTitle)
					.channelTitle(channelTitle)
					.thumbnailUrl(thumbnailUrl)
					.youtubeUrl("https://www.youtube.com/watch?v=" + videoId)
					.build();
			} else {
				log.warn("YouTube 검색 결과 없음: {}", query);
				throw new RuntimeException("검색 결과가 없습니다.");
			}

		} catch (Exception e) {
			log.error("YouTube API 호출 실패: {}", e.getMessage());
			throw new RuntimeException("YouTube 검색 중 오류가 발생했습니다: " + e.getMessage());
		}
	}

	@Transactional
	public void updateSongWithYoutubeInfo(Song song) {
		try {
			YouTubeSearchResponse response = searchVideo(song.getTitle(), song.getArtist());
			song.updateYoutubeUrl(response.getYoutubeUrl());
			songRepository.save(song);
			log.info("Song YouTube 정보 업데이트 완료: songId={}", song.getId());
		} catch (Exception e) {
			log.error("Song YouTube 정보 업데이트 실패: songId={}, error={}", song.getId(), e.getMessage());
		}
	}
}