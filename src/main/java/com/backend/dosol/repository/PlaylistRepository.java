package com.backend.dosol.repository;

import com.backend.dosol.entity.Playlist;
import com.backend.dosol.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findTop5ByUserOrderByCreatedAtDesc(User user);
}