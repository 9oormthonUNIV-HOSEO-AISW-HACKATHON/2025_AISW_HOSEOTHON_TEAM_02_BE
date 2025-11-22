package com.backend.dosol.repository;

import com.backend.dosol.entity.Review;
import com.backend.dosol.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	List<Review> findAllByTargetUser(User targetUser);
}
