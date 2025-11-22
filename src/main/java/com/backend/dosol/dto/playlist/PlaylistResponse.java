package com.backend.dosol.dto.playlist;

import com.backend.dosol.dto.song.SongResponse;
import com.backend.dosol.entity.Playlist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistResponse {

	private Long id;
	private List<SongResponse> songs;

	public static PlaylistResponse from(Playlist playlist, List<SongResponse> songs) {
		return PlaylistResponse.builder()
			.id(playlist.getId())
			.songs(songs)
			.build();
	}
}