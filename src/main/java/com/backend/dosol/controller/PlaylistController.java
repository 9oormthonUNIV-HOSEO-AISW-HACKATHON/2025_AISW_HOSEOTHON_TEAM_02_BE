package com.backend.dosol.controller;

import com.backend.dosol.dto.DataResponse;
import com.backend.dosol.dto.PlaylistCompleteResponse;
import com.backend.dosol.dto.PlaylistResponse;
import com.backend.dosol.dto.SongResponse;
import com.backend.dosol.entity.Playlist;
import com.backend.dosol.entity.PlaylistSong;
import com.backend.dosol.repository.PlaylistSongRepository;
import com.backend.dosol.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;
    private final PlaylistSongRepository playlistSongRepository;

    @PostMapping("/{playlistId}/complete")
    public ResponseEntity<DataResponse<PlaylistCompleteResponse>> completePlaylist(@PathVariable Long playlistId) {
        String authCode = playlistService.completePlaylist(playlistId);
        PlaylistCompleteResponse playlistData = PlaylistCompleteResponse.builder()
                .userCode(authCode)
                .build();
        return ResponseEntity.ok(DataResponse.of(playlistData));
    }

    @GetMapping("/auth/{authCode}")
    public ResponseEntity<DataResponse<List<PlaylistResponse>>> getPlaylistsByAuthCode(@PathVariable String authCode) {
        List<Playlist> playlists = playlistService.getPlaylistsByAuthCode(authCode);

        List<PlaylistResponse> playlistResponses = playlists.stream()
                .map(playlist -> {
                    List<SongResponse> songs = playlistSongRepository.findByPlaylist(playlist)
                            .stream()
                            .map(PlaylistSong::getSong)
                            .map(SongResponse::from)
                            .collect(Collectors.toList());
                    return PlaylistResponse.from(playlist, songs);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(DataResponse.of(playlistResponses));
    }
}