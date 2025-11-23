package com.backend.dosol.repository;

import com.backend.dosol.entity.Song;
import com.backend.dosol.entity.type.Genre;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByGenerationAndGenre(String generation, Genre genre);

    List<Song> findByGenerationAndGenre(String generation, Genre genre, Pageable pageable);
}