package com.backend.dosol.dto.playlist;

import com.backend.dosol.entity.type.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PlaylistRegisterRequest {

	@Schema(description = "사용자 닉네임", example = "감성파 리스너")
	private String nickname;

	@Schema(description = "사용자 세대", example = "gen1")
	private String generation;

	@Schema(description = "선호 장르", example = "EMOTIONAL")
	private Genre favoriteGenre;

	@Schema(description = "플레이리스트 제목", example = "나의 첫 플레이리스트")
	private String playlistTitle;

	@Schema(description = "플레이리스트에 포함될 노래 ID 목록", example = "[1, 2, 3]")
	private List<Long> songIds;
}
