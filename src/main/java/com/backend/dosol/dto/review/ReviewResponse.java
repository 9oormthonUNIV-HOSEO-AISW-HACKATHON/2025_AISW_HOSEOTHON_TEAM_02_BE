package com.backend.dosol.dto.review;

import com.backend.dosol.entity.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class ReviewResponse {

	private Long reviewId;
	private String content;
	private String createdAt;

	public static ReviewResponse from(Review review) {
		return ReviewResponse.builder()
			.reviewId(review.getId())
			.content(review.getContent())
			.createdAt(review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
			.build();
	}
}
