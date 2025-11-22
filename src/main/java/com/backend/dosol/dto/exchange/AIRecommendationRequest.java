package com.backend.dosol.dto.exchange;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AIRecommendationRequest {

	@Schema(description = "사용자의 현재 기분", example = "신나는")
	private String mood;

	@Schema(description = "추천을 요청하는 사용자의 고유 코드", example = "B6206F")
	private String userCode;
}
