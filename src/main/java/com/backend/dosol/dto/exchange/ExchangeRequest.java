package com.backend.dosol.dto.exchange;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExchangeRequest {

	@Schema(description = "저장할 플레이리스트의 ID", example = "1")
	private Long playlistId;

	@Schema(description = "플레이리스트를 저장하는 사용자의 고유 코드", example = "B6206F")
	private String userCode;
}
