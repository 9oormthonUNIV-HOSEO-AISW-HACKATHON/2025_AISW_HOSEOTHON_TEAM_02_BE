package com.backend.dosol.controller;

import com.backend.dosol.dto.common.DataResponse;
import com.backend.dosol.dto.song.SongResponse;
import com.backend.dosol.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/songs")
@Tag(name = "Songs", description = "노래 API")
public class SongController {

	private final SongService songService;

	@GetMapping("/candidates/{userCode}")
	@Operation(summary = "유저 맞춤 노래 후보 조회", description = "유저의 세대와 장르에 맞는 노래 후보 목록을 조회합니다.")
	public ResponseEntity<DataResponse<List<SongResponse>>> getSongCandidates(
		@PathVariable String userCode) {
		List<SongResponse> songResponses = songService.getSongCandidates(userCode);
		return ResponseEntity.ok(DataResponse.of(songResponses));
	}
}
