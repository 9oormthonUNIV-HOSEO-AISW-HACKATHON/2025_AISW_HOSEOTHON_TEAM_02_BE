package com.backend.dosol.dto.review;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequest {

	private String writerUserCode;
	private String targetUserCode;
	private String content;
}
