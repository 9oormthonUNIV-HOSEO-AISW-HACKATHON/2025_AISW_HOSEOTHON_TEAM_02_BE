package com.backend.dosol.repository;

import com.backend.dosol.entity.User;
import com.backend.dosol.entity.type.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByAuthCode(String authCode);

	List<User> findByFavoriteGenreAndGenerationNot(Genre favoriteGenre, String generation);
}