package com.backend.dosol.dto.playlist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistCompleteResponse {

	private String userCode;
	private Long playlistId;
}