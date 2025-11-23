package com.backend.dosol.controller;

import com.backend.dosol.dto.common.DataResponse;
import com.backend.dosol.dto.playlist.PlaylistCompleteResponse;
import com.backend.dosol.dto.playlist.PlaylistRegisterRequest;
import com.backend.dosol.dto.playlist.PlaylistResponse;
import com.backend.dosol.dto.song.SongResponse;
import com.backend.dosol.entity.Playlist;
import com.backend.dosol.service.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Playlists", description = "플레이리스트 API")
@RestController
@RequestMapping("/api/v1/playlists")
@RequiredArgsConstructor
public class PlaylistController {

	private final PlaylistService playlistService;

	@PostMapping("/register")
	@Operation(summary = "플레이리스트 등록 및 유저 생성", description = "사용자 정보와 노래 목록을 받아 플레이리스트를 생성하고, 고유 userCode를 발급합니다.")
	public ResponseEntity<DataResponse<PlaylistCompleteResponse>> registerPlaylist(
		@RequestBody PlaylistRegisterRequest request) {
		PlaylistCompleteResponse response = playlistService.registerPlaylist(request);
		return ResponseEntity.ok(DataResponse.of(response));
	}

	@GetMapping("/{playlistId}")
	@Operation(summary = "플레이리스트 상세 조회", description = "플레이리스트 ID로 해당 플레이리스트의 상세 정보와 수록곡 목록(유튜브 URL 포함)을 조회합니다.")
	public ResponseEntity<DataResponse<PlaylistResponse>> getPlaylist(
		@Parameter(description = "조회할 플레이리스트의 ID", example = "1") @PathVariable Long playlistId) {
		Playlist playlist = playlistService.getPlaylist(playlistId);

		List<SongResponse> songResponses = playlist.getPlaylistSongs().stream()
			.map(playlistSong -> SongResponse.from(playlistSong.getSong()))
			.collect(Collectors.toList());

		PlaylistResponse playlistResponse = PlaylistResponse.from(playlist, songResponses);

		return ResponseEntity.ok(DataResponse.of(playlistResponse));
	}
}