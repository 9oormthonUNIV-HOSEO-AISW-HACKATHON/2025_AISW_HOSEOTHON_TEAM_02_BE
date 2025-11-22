package com.backend.dosol.dto.user;

import com.backend.dosol.entity.User;
import com.backend.dosol.entity.type.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserRecommendationResponse {
    @Schema(description = "추천된 사용자의 닉네임", example = "음악탐험가")
    private final String nickname;

    @Schema(description = "추천된 사용자의 세대", example = "gen2")
    private final String generation;

    @Schema(description = "추천된 사용자의 선호 장르", example = "DANCE")
    private final Genre favoriteGenre;

    @Schema(description = "추천된 사용자의 고유 코드", example = "ABC123")
    private final String userCode;

    @Builder
    public UserRecommendationResponse(String nickname, String generation, Genre favoriteGenre, String userCode) {
        this.nickname = nickname;
        this.generation = generation;
        this.favoriteGenre = favoriteGenre;
        this.userCode = userCode;
    }

    public static UserRecommendationResponse from(User user) {
        return UserRecommendationResponse.builder()
                .nickname(user.getNickname())
                .generation(user.getGeneration())
                .favoriteGenre(user.getFavoriteGenre())
                .userCode(user.getAuthCode())
                .build();
    }
}
