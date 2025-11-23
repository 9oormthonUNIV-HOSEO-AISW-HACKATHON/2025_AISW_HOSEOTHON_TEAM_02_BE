package com.backend.dosol.service;

import com.backend.dosol.dto.review.ReviewRequest;
import com.backend.dosol.entity.Review;
import com.backend.dosol.entity.User;
import com.backend.dosol.repository.ReviewRepository;
import com.backend.dosol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final UserRepository userRepository;

	@Transactional
	public void createReview(ReviewRequest request) {
		User writer = userRepository.findByAuthCode(request.getWriterUserCode())
			.orElseThrow(() -> new RuntimeException("작성자를 찾을 수 없습니다."));

		User targetUser = userRepository.findByAuthCode(request.getTargetUserCode())
			.orElseThrow(() -> new RuntimeException("리뷰 대상을 찾을 수 없습니다."));

		Review review = Review.builder()
			.writer(writer)
			.targetUser(targetUser)
			.content(request.getContent())
			.build();

		reviewRepository.save(review);
	}

	@Transactional(readOnly = true)
	public List<Review> getMyReviews(String userCode) {
		User targetUser = userRepository.findByAuthCode(userCode)
			.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

		return reviewRepository.findAllByTargetUser(targetUser);
	}

	@Transactional(readOnly = true)
	public User getUserByCode(String userCode) {
		return userRepository.findByAuthCode(userCode)
			.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
	}
}
