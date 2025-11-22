package com.backend.dosol.controller;

import com.backend.dosol.dto.common.DataResponse;
import com.backend.dosol.dto.exchange.AIRecommendationRequest;
import com.backend.dosol.dto.exchange.AIRecommendationResponse;
import com.backend.dosol.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AIController {

	private final ExchangeService exchangeService;

	@PostMapping("/recommendations")
	public ResponseEntity<DataResponse<AIRecommendationResponse>> getAIRecommendation(
		@RequestBody AIRecommendationRequest request) {
		AIRecommendationResponse response = exchangeService.getAIRecommendation(request);
		return ResponseEntity.ok(DataResponse.of(response));
	}
}
