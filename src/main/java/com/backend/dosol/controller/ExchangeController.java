package com.backend.dosol.controller;

import com.backend.dosol.dto.common.DataResponse;
import com.backend.dosol.dto.exchange.ExchangeRequest;
import com.backend.dosol.dto.playlist.PlaylistResponse;
import com.backend.dosol.dto.song.SongResponse;
import com.backend.dosol.entity.Playlist;
import com.backend.dosol.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/exchanges")
@RequiredArgsConstructor
public class ExchangeController {

	private final ExchangeService exchangeService;

	@PostMapping
	public ResponseEntity<Void> exchangePlaylist(@RequestBody ExchangeRequest request) {
		exchangeService.saveExchange(request);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{userCode}")
	public ResponseEntity<DataResponse<List<PlaylistResponse>>> getMyExchanges(
		@PathVariable String userCode) {
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
