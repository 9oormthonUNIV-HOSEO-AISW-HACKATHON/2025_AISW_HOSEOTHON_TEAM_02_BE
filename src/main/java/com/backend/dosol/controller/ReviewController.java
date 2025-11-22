package com.backend.dosol.controller;

import com.backend.dosol.dto.common.DataResponse;
import com.backend.dosol.dto.review.MyReviewsResponse;
import com.backend.dosol.dto.review.ReviewRequest;
import com.backend.dosol.dto.review.ReviewResponse;
import com.backend.dosol.entity.Review;
import com.backend.dosol.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	@PostMapping
	public ResponseEntity<Void> createReview(@RequestBody ReviewRequest request) {
		reviewService.createReview(request);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{userCode}")
	public ResponseEntity<DataResponse<MyReviewsResponse>> getMyReviews(
		@PathVariable String userCode) {
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
