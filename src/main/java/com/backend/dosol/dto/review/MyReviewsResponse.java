package com.backend.dosol.dto.review;

import com.backend.dosol.entity.type.Genre;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyReviewsResponse {

	private String ownerCode;
	private String generation;
	private Genre favoriteGenre;
	private List<ReviewResponse> reviews;
}

