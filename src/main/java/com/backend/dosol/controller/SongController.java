package com.backend.dosol.controller;

import com.backend.dosol.dto.common.DataResponse;
import com.backend.dosol.dto.song.SongResponse;
import com.backend.dosol.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/songs")
@Tag(name = "Songs", description = "노래 API")
public class SongController {

	private final SongService songService;

	@GetMapping("/candidates")
	@Operation(summary = "세대와 장르별 노래 후보 조회", description = "요청된 세대와 장르에 맞는 노래 후보 목록을 조회합니다.")
	public ResponseEntity<DataResponse<List<SongResponse>>> getSongCandidates(
		@Parameter(description = "조회할 세대", example = "gen1") @RequestParam String generation,
		@Parameter(description = "조회할 장르", example = "dance") @RequestParam String genre) {
		List<SongResponse> songResponses = songService.getSongCandidates(generation, genre);
		return ResponseEntity.ok(DataResponse.of(songResponses));
	}
}
