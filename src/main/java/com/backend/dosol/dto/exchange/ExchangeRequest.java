package com.backend.dosol.dto.exchange;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExchangeRequest {

	private Long playlistId;
	private String userCode; // The user who is saving the playlist
}
