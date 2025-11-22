package com.backend.dosol.dto.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequest {

	@Schema(description = "리뷰 작성자의 고유 코드", example = "B6206F")
	private String writerUserCode;

	@Schema(description = "리뷰 대상(플레이리스트 주인)의 고유 코드", example = "A1B2C3")
	private String targetUserCode;

	@Schema(description = "리뷰 내용", example = "노래 너무 좋네요!")
	private String content;
}
