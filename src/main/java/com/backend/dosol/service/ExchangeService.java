package com.backend.dosol.service;

import com.backend.dosol.dto.exchange.AIRecommendationRequest;
import com.backend.dosol.dto.exchange.AIRecommendationResponse;
import com.backend.dosol.dto.exchange.ExchangeRequest;
import com.backend.dosol.entity.Exchange;
import com.backend.dosol.entity.Playlist;
import com.backend.dosol.entity.User;
import com.backend.dosol.repository.ExchangeRepository;
import com.backend.dosol.repository.PlaylistRepository;
import com.backend.dosol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExchangeService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final ExchangeRepository exchangeRepository;

    @Transactional(readOnly = true)
    public AIRecommendationResponse getAIRecommendation(AIRecommendationRequest request) {
        // AI 로직 대신, 랜덤으로 플레이리스트 하나를 추천하는 로직
        // TODO: 실제 AI 추천 로직 구현
        List<Playlist> allPlaylists = playlistRepository.findAll();
        if (allPlaylists.isEmpty()) {
            throw new RuntimeException("추천할 플레이리스트가 없습니다.");
        }

        Playlist recommendedPlaylist = allPlaylists.get(new Random().nextInt(allPlaylists.size()));

        // 요약 정보 생성 (플레이리스트의 첫 3개 곡 제목으로)
        String summary = recommendedPlaylist.getPlaylistSongs().stream()
                .map(ps -> ps.getSong().getTitle())
                .limit(3)
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElse("다양한 곡들이 담겨있어요.");

        return AIRecommendationResponse.builder()
                .recommendedPlaylistId(recommendedPlaylist.getId())
                .playlistTitle(recommendedPlaylist.getTitle())
                .ownerCode(recommendedPlaylist.getUser().getAuthCode())
                .summary(summary)
                .build();
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
