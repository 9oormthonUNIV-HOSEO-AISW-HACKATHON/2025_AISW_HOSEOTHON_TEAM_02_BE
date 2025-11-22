package com.backend.dosol.service;

import com.backend.dosol.dto.playlist.PlaylistCompleteResponse;
import com.backend.dosol.dto.playlist.PlaylistRegisterRequest;
import com.backend.dosol.entity.Playlist;
import com.backend.dosol.entity.PlaylistSong;
import com.backend.dosol.entity.Song;
import com.backend.dosol.entity.User;
import com.backend.dosol.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaylistService {

	private final PlaylistRepository playlistRepository;
	private final PlaylistSongRepository playlistSongRepository;
	private final UserRepository userRepository;
	private final SongRepository songRepository;
	private final YouTubeService youtubeService;

	@Transactional
	public PlaylistCompleteResponse registerPlaylist(PlaylistRegisterRequest request) {
		// 1. User 생성 또는 조회 (닉네임 기반) - 여기서는 간단하게 매번 생성
		User user = User.builder()
			.nickname(request.getNickname())
			.generation(request.getGeneration())
			.favoriteGenre(request.getFavoriteGenre())
			.build();
		user = userRepository.save(user);

		// 2. Playlist 생성
		Playlist playlist = Playlist.builder()
			.user(user)
			.title(request.getPlaylistTitle())
			.build();
		playlist = playlistRepository.save(playlist);

		// 3. Song 찾아서 PlaylistSong 으로 연결
		List<Song> songs = songRepository.findAllById(request.getSongIds());
		for (Song song : songs) {
			playlistSongRepository.save(
				PlaylistSong.builder().playlist(playlist).song(song).build());
			// YouTube URL이 없으면 채워넣기
			if (song.getYoutubeUrl() == null || song.getYoutubeUrl().isEmpty()) {
				youtubeService.updateSongWithYoutubeInfo(song);
			}
		}

		// 4. User에 authCode 발급
		String authCode = generateAuthCode();
		user.updateAuthCode(authCode);
		userRepository.save(user); // 변경 감지가 동작하지만 명시적으로 save 호출

		log.info("플레이리스트 등록 완료: playlistId={}, userCode={}", playlist.getId(), authCode);

		return PlaylistCompleteResponse.builder()
			.playlistId(playlist.getId())
			.userCode(authCode)
			.build();
	}


	private String generateAuthCode() {
		String authCode;
		do {
			authCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
		} while (userRepository.findByAuthCode(authCode).isPresent());

		return authCode;
	}

	@Transactional(readOnly = true)
	public Playlist getPlaylist(Long playlistId) {
		return playlistRepository.findById(playlistId)
			.orElseThrow(() -> new RuntimeException("플레이리스트를 찾을 수 없습니다."));
	}
}