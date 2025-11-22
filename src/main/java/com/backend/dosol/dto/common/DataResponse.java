package com.backend.dosol.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataResponse<T> {

	private T data;

	public static <T> DataResponse<T> of(T data) {
		return DataResponse.<T>builder()
			.data(data)
			.build();
	}
}