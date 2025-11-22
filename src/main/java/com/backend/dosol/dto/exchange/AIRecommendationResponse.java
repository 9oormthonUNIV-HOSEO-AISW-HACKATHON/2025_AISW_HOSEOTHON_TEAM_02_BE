package com.backend.dosol.dto.exchange;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AIRecommendationResponse {

	private Long recommendedPlaylistId;
	private String playlistTitle;
	private String ownerCode;
	private String summary;
}
