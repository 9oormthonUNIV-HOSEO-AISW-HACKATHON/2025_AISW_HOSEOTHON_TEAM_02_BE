package com.backend.dosol.config;

import com.backend.dosol.entity.Playlist;
import com.backend.dosol.entity.PlaylistSong;
import com.backend.dosol.entity.Song;
import com.backend.dosol.entity.User;
import com.backend.dosol.entity.type.Genre;
import com.backend.dosol.repository.PlaylistRepository;
import com.backend.dosol.repository.PlaylistSongRepository;
import com.backend.dosol.repository.SongRepository;
import com.backend.dosol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;
    private final PlaylistSongRepository playlistSongRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("더미 데이터 생성 시작...");

        // 1. User 생성 (세대: 2, 장르: HIPHOP 고정)
        User user = User.builder()
                .nickname("테스트유저")
                .generation("2")
                .favoriteGenre(Genre.HIPHOP)
                .build();
        user = userRepository.save(user);
        log.info("User 생성 완료: id={}, nickname={}", user.getId(), user.getNickname());

        // 2. Song 5개 생성
        Song song1 = Song.builder()
                .title("APT.")
                .artist("로제 브루노마스")
                .build();

        Song song2 = Song.builder()
                .title("Supernova")
                .artist("aespa")
                .build();

        Song song3 = Song.builder()
                .title("Dynamite")
                .artist("BTS")
                .build();

        Song song4 = Song.builder()
                .title("Love wins all")
                .artist("IU")
                .build();

        Song song5 = Song.builder()
                .title("How Sweet")
                .artist("NewJeans")
                .build();

        song1 = songRepository.save(song1);
        song2 = songRepository.save(song2);
        song3 = songRepository.save(song3);
        song4 = songRepository.save(song4);
        song5 = songRepository.save(song5);
        log.info("Song 5개 생성 완료");

        // 3. Playlist 생성
        Playlist playlist = Playlist.builder()
                .user(user)
                .build();
        playlist = playlistRepository.save(playlist);
        log.info("Playlist 생성 완료: id={}", playlist.getId());

        // 4. PlaylistSong 연결
        playlistSongRepository.save(PlaylistSong.builder().playlist(playlist).song(song1).build());
        playlistSongRepository.save(PlaylistSong.builder().playlist(playlist).song(song2).build());
        playlistSongRepository.save(PlaylistSong.builder().playlist(playlist).song(song3).build());
        playlistSongRepository.save(PlaylistSong.builder().playlist(playlist).song(song4).build());
        playlistSongRepository.save(PlaylistSong.builder().playlist(playlist).song(song5).build());
        log.info("PlaylistSong 5개 연결 완료");

        log.info("더미 데이터 생성 완료!");
        log.info("테스트용 Playlist ID: {}", playlist.getId());
        log.info("POST /api/playlists/{}/complete 를 호출하여 authCode를 생성하세요", playlist.getId());
    }
}