package com.backend.dosol.dto.youtube;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeSearchRequest {

	private String title;
	private String artist;
}