package com.backend.dosol.controller;

import com.backend.dosol.dto.common.DataResponse;
import com.backend.dosol.dto.review.MyReviewsResponse;
import com.backend.dosol.dto.review.ReviewRequest;
import com.backend.dosol.dto.review.ReviewResponse;
import com.backend.dosol.entity.Review;
import com.backend.dosol.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Reviews", description = "감상평(리뷰) API")
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	@PostMapping
	@Operation(summary = "감상평 작성", description = "교환받은 플레이리스트에 대한 감상평을 작성합니다.")
	public ResponseEntity<Void> createReview(@RequestBody ReviewRequest request) {
		reviewService.createReview(request);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{userCode}")
	@Operation(summary = "나에게 달린 감상평 목록 조회", description = "나의 userCode로 다른 사용자들이 내 플레이리스트에 대해 작성한 모든 감상평 목록을 조회합니다.")
	public ResponseEntity<DataResponse<MyReviewsResponse>> getMyReviews(
		@Parameter(description = "조회할 사용자의 고유 코드", example = "A1B2C3") @PathVariable String userCode) {
		List<Review> reviews = reviewService.getMyReviews(userCode);

		List<ReviewResponse> reviewResponses = reviews.stream()
			.map(ReviewResponse::from)
			.collect(Collectors.toList());

		MyReviewsResponse response = MyReviewsResponse.builder()
			.ownerCode(userCode)
			.reviews(reviewResponses)
			.build();

		return ResponseEntity.ok(DataResponse.of(response));
	}
}
