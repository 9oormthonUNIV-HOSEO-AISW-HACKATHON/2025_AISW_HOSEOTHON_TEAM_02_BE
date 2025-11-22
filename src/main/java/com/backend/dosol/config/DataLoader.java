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

		// 1. User 생성
		User user = User.builder()
			.nickname("테스트유저")
			.generation("2")
			.favoriteGenre(Genre.HIPHOP)
			.build();
		user = userRepository.save(user);
		log.info("User 생성 완료: id={}", user.getId());

		// 2. Song 5개 생성 (유튜브 URL 추가)
		Song song1 = Song.builder()
			.title("APT.")
			.artist("로제 브루노마스")
			.generation("2020년대")
			.genre(Genre.DANCE)
			.youtubeUrl("https://www.youtube.com/watch?v=ekr2nIex040") // 추가
			.build();

		Song song2 = Song.builder()
			.title("Supernova")
			.artist("aespa")
			.generation("2020년대")
			.genre(Genre.DANCE)
			.youtubeUrl("https://www.youtube.com/watch?v=phuiiNCxRMg") // 추가
			.build();

		Song song3 = Song.builder()
			.title("Dynamite")
			.artist("BTS")
			.generation("2020년대")
			.genre(Genre.DANCE)
			.youtubeUrl("https://www.youtube.com/watch?v=gdZLi9oWNZg") // 추가
			.build();

		Song song4 = Song.builder()
			.title("Love wins all")
			.artist("IU")
			.generation("2020년대")
			.genre(Genre.EMOTIONAL)
			.youtubeUrl("https://www.youtube.com/watch?v=JleoAppaxi0") // 추가
			.build();

		Song song5 = Song.builder()
			.title("How Sweet")
			.artist("NewJeans")
			.generation("2020년대")
			.genre(Genre.DANCE)
			.youtubeUrl("https://www.youtube.com/watch?v=Q3K0TOvTOno") // 추가
			.build();

		song1 = songRepository.save(song1);
		song2 = songRepository.save(song2);
		song3 = songRepository.save(song3);
		song4 = songRepository.save(song4);
		song5 = songRepository.save(song5);
		log.info("Song 5개 생성 완료");

		// 3. Playlist 생성 (수정된 부분!)
		Playlist playlist = Playlist.builder()
			.user(user)
			.title("테스트유저의 플레이리스트") // [중요] title 필수값 추가!
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
	}
}