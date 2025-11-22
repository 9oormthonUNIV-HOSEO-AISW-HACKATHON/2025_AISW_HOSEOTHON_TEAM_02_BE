package com.backend.dosol.repository;

import com.backend.dosol.entity.Playlist;
import com.backend.dosol.entity.PlaylistSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, Long> {
    List<PlaylistSong> findByPlaylist(Playlist playlist);
}
