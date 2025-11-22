package com.backend.dosol.dto.playlist;

import com.backend.dosol.entity.type.Genre;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PlaylistRegisterRequest {

	private String nickname;
	private String generation;
	private Genre favoriteGenre;
	private String playlistTitle;
	private List<Long> songIds;
}
