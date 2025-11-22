package com.backend.dosol.dto.user;

import com.backend.dosol.dto.song.SongResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UserRecommendationDetailResponse {

    @Schema(description = "추천된 사용자의 고유 코드", example = "XYZ789")
    private final String targetUserCode;

    @Schema(description = "추천된 플레이리스트의 ID", example = "1")
    private final Long playlistId;

    @Schema(description = "추천된 사용자의 플레이리스트에 담긴 노래 목록")
    private final List<SongResponse> songs;

    @Builder
    public UserRecommendationDetailResponse(String targetUserCode, Long playlistId, List<SongResponse> songs) {
        this.targetUserCode = targetUserCode;
        this.playlistId = playlistId;
        this.songs = songs;
    }
}
