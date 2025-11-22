package com.backend.dosol.controller;

import com.backend.dosol.dto.YouTubeSearchRequest;
import com.backend.dosol.dto.YouTubeSearchResponse;
import com.backend.dosol.service.YouTubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/youtube")
@RequiredArgsConstructor
public class YouTubeController {

    private final YouTubeService youtubeService;

    @PostMapping("/search")
    public ResponseEntity<YouTubeSearchResponse> searchVideo(@RequestBody YouTubeSearchRequest request) {
        YouTubeSearchResponse response = youtubeService.searchVideo(request.getTitle(), request.getArtist());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<YouTubeSearchResponse> searchVideoByParams(
            @RequestParam String title,
            @RequestParam String artist) {
        YouTubeSearchResponse response = youtubeService.searchVideo(title, artist);
        return ResponseEntity.ok(response);
    }
}