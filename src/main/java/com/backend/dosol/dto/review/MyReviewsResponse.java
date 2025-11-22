package com.backend.dosol.dto.review;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyReviewsResponse {

	private String ownerCode;
	private List<ReviewResponse> reviews;
}

