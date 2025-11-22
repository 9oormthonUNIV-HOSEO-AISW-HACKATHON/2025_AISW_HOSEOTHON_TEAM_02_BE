package com.backend.dosol.controller;

import com.backend.dosol.dto.common.DataResponse;
import com.backend.dosol.dto.playlist.PlaylistCompleteResponse;
import com.backend.dosol.dto.playlist.PlaylistRegisterRequest;
import com.backend.dosol.dto.playlist.PlaylistResponse;
import com.backend.dosol.dto.song.SongResponse;
import com.backend.dosol.entity.Playlist;
import com.backend.dosol.entity.Song;
import com.backend.dosol.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/playlists")
@RequiredArgsConstructor
public class PlaylistController {

	private final PlaylistService playlistService;

	@PostMapping("/register")
	public ResponseEntity<DataResponse<PlaylistCompleteResponse>> registerPlaylist(
		@RequestBody PlaylistRegisterRequest request) {
		PlaylistCompleteResponse response = playlistService.registerPlaylist(request);
		return ResponseEntity.ok(DataResponse.of(response));
	}

	@GetMapping("/{playlistId}")
	public ResponseEntity<DataResponse<PlaylistResponse>> getPlaylist(
		@PathVariable Long playlistId) {
		Playlist playlist = playlistService.getPlaylist(playlistId);

		List<SongResponse> songResponses = playlist.getPlaylistSongs().stream()
			.map(playlistSong -> SongResponse.from(playlistSong.getSong()))
			.collect(Collectors.toList());

		PlaylistResponse playlistResponse = PlaylistResponse.from(playlist, songResponses);

		return ResponseEntity.ok(DataResponse.of(playlistResponse));
	}
}