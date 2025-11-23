package com.backend.dosol.service;

import com.backend.dosol.dto.exchange.AIRecommendationRequest;
import com.backend.dosol.dto.exchange.ExchangeRequest;
import com.backend.dosol.dto.song.SongResponse;
import com.backend.dosol.dto.user.UserRecommendationDetailResponse;
import com.backend.dosol.dto.user.UserRecommendationResponse;
import com.backend.dosol.entity.Exchange;
import com.backend.dosol.entity.Playlist;
import com.backend.dosol.entity.PlaylistSong;
import com.backend.dosol.entity.User;
import com.backend.dosol.repository.ExchangeRepository;
import com.backend.dosol.repository.PlaylistRepository;
import com.backend.dosol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExchangeService {

	private final PlaylistRepository playlistRepository;
	private final UserRepository userRepository;
	private final ExchangeRepository exchangeRepository;

	@Transactional(readOnly = true)
	public UserRecommendationDetailResponse recommendUser(String userCode) {
		// 1. 요청 사용자 조회
		User requestingUser = userRepository.findByAuthCode(userCode)
			.orElseThrow(() -> new RuntimeException("요청 사용자를 찾을 수 없습니다: " + userCode));

		// 2. 같은 장르, 다른 세대의 사용자들 조회
		List<User> candidateUsers = userRepository.findByFavoriteGenreAndGenerationNot(
			requestingUser.getFavoriteGenre(), requestingUser.getGeneration()
		);

		// 3. 자기 자신은 제외하고, 순서를 섞는다
		List<User> otherUsers = candidateUsers.stream()
			.filter(user -> !Objects.equals(user.getId(), requestingUser.getId()))
			.collect(Collectors.toList());
		Collections.shuffle(otherUsers);

		// 4. 플레이리스트를 가진 첫 번째 사용자를 찾는다
		for (User recommendedUser : otherUsers) {
			List<Playlist> playlists = playlistRepository.findTop5ByUserOrderByCreatedAtDesc(
				recommendedUser);
			if (!playlists.isEmpty()) {
				Playlist playlistToShare = playlists.get(0); // 첫 번째 플레이리스트를 공유
				List<SongResponse> songs = playlistToShare.getPlaylistSongs().stream()
					.map(PlaylistSong::getSong)
					.map(SongResponse::from)
					.collect(Collectors.toList());
				return UserRecommendationDetailResponse.builder()
					.targetUserCode(recommendedUser.getAuthCode())
					.playlistId(playlistToShare.getId())
					.songs(songs)
					.build();
			}
		}

		throw new RuntimeException("추천할 사용자를 찾을 수 없거나, 추천된 사용자의 플레이리스트가 없습니다.");
	}

	@Transactional
	public void saveExchange(ExchangeRequest request) {
		User receiver = userRepository.findByAuthCode(request.getUserCode())
			.orElseThrow(() -> new RuntimeException("받는 사람을 찾을 수 없습니다."));

		Playlist playlist = playlistRepository.findById(request.getPlaylistId())
			.orElseThrow(() -> new RuntimeException("플레이리스트를 찾을 수 없습니다."));

		User sender = playlist.getUser();

		if (receiver.getId().equals(sender.getId())) {
			throw new RuntimeException("자신의 플레이리스트는 저장할 수 없습니다.");
		}

		Exchange exchange = Exchange.builder()
			.sender(sender)
			.receiver(receiver)
			.playlist(playlist)
			.build();

		exchangeRepository.save(exchange);
	}

	@Transactional(readOnly = true)
	public List<Playlist> getMyExchangedPlaylists(String userCode) {
		User receiver = userRepository.findByAuthCode(userCode)
			.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

		return exchangeRepository.findAllByReceiver(receiver).stream()
			.map(Exchange::getPlaylist)
			.collect(Collectors.toList());
	}
}
