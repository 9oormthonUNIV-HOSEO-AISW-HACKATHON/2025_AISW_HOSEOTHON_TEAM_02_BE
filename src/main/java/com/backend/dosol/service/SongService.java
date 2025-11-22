package com.backend.dosol.service;

import com.backend.dosol.dto.song.SongResponse;
import com.backend.dosol.entity.Song;
import com.backend.dosol.entity.type.Genre;
import com.backend.dosol.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SongService {

    private final SongRepository songRepository;

    public List<SongResponse> getSongCandidates(String generation, String genre) {
        Genre genreEnum = Genre.valueOf(genre.toUpperCase());
        List<Song> songs = songRepository.findByGenerationAndGenre(generation, genreEnum);

        return songs.stream()
                .map(SongResponse::from)
                .collect(Collectors.toList());
    }
}
