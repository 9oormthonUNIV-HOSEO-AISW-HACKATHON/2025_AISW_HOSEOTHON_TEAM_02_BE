package com.backend.dosol.dto;

import com.backend.dosol.entity.Song;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongResponse {
    private Long id;
    private String title;
    private String artist;
    private String youtubeVideoId;
    private String youtubeUrl;

    public static SongResponse from(Song song) {
        return SongResponse.builder()
                .id(song.getId())
                .title(song.getTitle())
                .artist(song.getArtist())
                .youtubeVideoId(song.getYoutubeVideoId())
                .youtubeUrl(song.getYoutubeUrl())
                .build();
    }
}