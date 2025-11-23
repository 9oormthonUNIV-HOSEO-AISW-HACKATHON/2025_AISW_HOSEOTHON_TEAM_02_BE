package com.backend.dosol.controller;

import com.backend.dosol.dto.common.DataResponse;
import com.backend.dosol.dto.user.UserRecommendationDetailResponse;
import com.backend.dosol.service.ExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User / Recommendation", description = "사용자 및 추천 관련 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final ExchangeService exchangeService;

    @GetMapping("/recommendations")
    @Operation(summary = "다른 세대, 같은 장르 사용자 추천", description = "자신과 선호 장르는 같지만 세대는 다른 사용자를 랜덤으로 추천받고, 해당 사용자의 플레이리스트 노래 목록을 함께 조회합니다.")
    public ResponseEntity<DataResponse<UserRecommendationDetailResponse>> recommendUser(
            @Parameter(description = "추천을 요청하는 사용자의 고유 코드", required = true, example = "B7V2A1") @RequestParam String userCode) {
        UserRecommendationDetailResponse response = exchangeService.recommendUser(userCode);
        return ResponseEntity.ok(DataResponse.of(response));
    }
}
