package com.backend.dosol.controller;

import com.backend.dosol.dto.common.DataResponse;
import com.backend.dosol.dto.exchange.AIRecommendationRequest;
import com.backend.dosol.dto.exchange.AIRecommendationResponse;
import com.backend.dosol.service.ExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AI", description = "AI 추천 API")
@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AIController {

	private final ExchangeService exchangeService;

	@PostMapping("/recommendations")
	@Operation(summary = "AI 플레이리스트 추천", description = "사용자의 userCode와 mood를 기반으로, 자신의 것을 제외한 다른 사용자의 플레이리스트 중 하나를 추천받습니다.")
	public ResponseEntity<DataResponse<AIRecommendationResponse>> getAIRecommendation(
		@RequestBody AIRecommendationRequest request) {
		AIRecommendationResponse response = exchangeService.getAIRecommendation(request);
		return ResponseEntity.ok(DataResponse.of(response));
	}
}
