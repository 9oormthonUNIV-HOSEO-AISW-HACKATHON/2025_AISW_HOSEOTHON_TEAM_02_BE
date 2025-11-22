package com.backend.dosol.service;

import com.backend.dosol.dto.song.SongResponse;
import com.backend.dosol.entity.Song;
import com.backend.dosol.entity.User;
import com.backend.dosol.repository.SongRepository;
import com.backend.dosol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SongService {

    private final UserRepository userRepository;
    private final SongRepository songRepository;

    public List<SongResponse> getSongCandidates(String userCode) {
        User user = userRepository.findByAuthCode(userCode)
                .orElseThrow(() -> new IllegalArgumentException("User not found with code: " + userCode));

        List<Song> songs = songRepository.findByGenerationAndGenre(user.getGeneration(), user.getFavoriteGenre());

        return songs.stream()
                .map(SongResponse::from)
                .collect(Collectors.toList());
    }
}
