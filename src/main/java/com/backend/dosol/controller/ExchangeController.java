package com.backend.dosol.controller;

import com.backend.dosol.dto.common.DataResponse;
import com.backend.dosol.dto.exchange.ExchangeRequest;
import com.backend.dosol.dto.playlist.PlaylistResponse;
import com.backend.dosol.dto.song.SongResponse;
import com.backend.dosol.entity.Playlist;
import com.backend.dosol.service.ExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Exchanges", description = "플레이리스트 교환(저장) API")
@RestController
@RequestMapping("/api/v1/exchanges")
@RequiredArgsConstructor
public class ExchangeController {

	private final ExchangeService exchangeService;

	@PostMapping
	@Operation(summary = "추천받은 플레이리스트 저장", description = "AI에게 추천받은 플레이리스트를 나의 보관함에 저장(교환)합니다.")
	public ResponseEntity<Void> exchangePlaylist(@RequestBody ExchangeRequest request) {
		exchangeService.saveExchange(request);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{userCode}")
	@Operation(summary = "내가 저장한 플레이리스트 목록 조회", description = "나의 userCode로 내가 저장한 모든 플레이리스트 목록을 조회합니다.")
	public ResponseEntity<DataResponse<List<PlaylistResponse>>> getMyExchanges(
		@Parameter(description = "조회할 사용자의 고유 코드", example = "B6206F") @PathVariable String userCode) {
		List<Playlist> playlists = exchangeService.getMyExchangedPlaylists(userCode);

		List<PlaylistResponse> response = playlists.stream().map(playlist -> {
			List<SongResponse> songResponses = playlist.getPlaylistSongs().stream()
				.map(ps -> SongResponse.from(ps.getSong()))
				.collect(Collectors.toList());
			return PlaylistResponse.from(playlist, songResponses);
		}).collect(Collectors.toList());

		return ResponseEntity.ok(DataResponse.of(response));
	}
}
