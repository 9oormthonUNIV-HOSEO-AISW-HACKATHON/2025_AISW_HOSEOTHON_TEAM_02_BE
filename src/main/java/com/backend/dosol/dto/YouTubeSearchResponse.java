package com.backend.dosol.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeSearchResponse {
    private String videoId;
    private String title;
    private String channelTitle;
    private String thumbnailUrl;
    private String youtubeUrl;
}