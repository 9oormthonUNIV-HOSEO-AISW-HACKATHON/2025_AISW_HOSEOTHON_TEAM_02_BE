package com.backend.dosol.dto.song;

import com.backend.dosol.entity.Song;
import com.backend.dosol.entity.type.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongResponse {

	private Long id;
	private String title;
	private String artist;
	private String generation;
	private String genre;
	private String youtubeUrl;

	public static SongResponse from(Song song) {
		return SongResponse.builder()
			.id(song.getId())
			.title(song.getTitle())
			.artist(song.getArtist())
			.generation(song.getGeneration())
			.genre(song.getGenre() != null ? song.getGenre().name() : null)
			.youtubeUrl(song.getYoutubeUrl())
			.build();
	}
}