package com.backend.dosol.service;

import com.backend.dosol.entity.Playlist;
import com.backend.dosol.entity.PlaylistSong;
import com.backend.dosol.entity.Song;
import com.backend.dosol.entity.User;
import com.backend.dosol.repository.PlaylistRepository;
import com.backend.dosol.repository.PlaylistSongRepository;
import com.backend.dosol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final PlaylistSongRepository playlistSongRepository;
    private final UserRepository userRepository;
    private final YouTubeService youtubeService;

    @Transactional
    public String completePlaylist(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("플레이리스트를 찾을 수 없습니다."));

        List<PlaylistSong> playlistSongs = playlistSongRepository.findByPlaylist(playlist);

        log.info("플레이리스트 완성 시작: playlistId={}, 곡 개수={}", playlistId, playlistSongs.size());

        for (PlaylistSong playlistSong : playlistSongs) {
            Song song = playlistSong.getSong();
            if (song.getYoutubeUrl() == null || song.getYoutubeUrl().isEmpty()) {
                youtubeService.updateSongWithYoutubeInfo(song);
            }
        }

        User user = playlist.getUser();
        if (user.getAuthCode() == null || user.getAuthCode().isEmpty()) {
            String authCode = generateAuthCode();
            user.updateAuthCode(authCode);
            userRepository.save(user);
            log.info("authCode 생성 완료: userId={}, authCode={}", user.getId(), authCode);
        }

        log.info("플레이리스트 완성 완료: playlistId={}", playlistId);
        return user.getAuthCode();
    }

    private String generateAuthCode() {
        String authCode;
        do {
            authCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        } while (userRepository.findByAuthCode(authCode).isPresent());

        return authCode;
    }

    @Transactional(readOnly = true)
    public List<Playlist> getPlaylistsByAuthCode(String authCode) {
        User user = userRepository.findByAuthCode(authCode)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 인증 코드입니다."));

        return playlistRepository.findTop5ByUserOrderByCreatedAtDesc(user);
    }
}