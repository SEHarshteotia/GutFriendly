package com.gutfriendly.app.reviews.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gutfriendly.app.reviews.model.UserReviews;

public interface UserReviewsRepository extends JpaRepository<UserReviews, Integer> {

	@Query("Select AVG(u.calculatedTrustScore) from UserReviews u ")
	Double getAverageUserRating();

	Page<UserReviews> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
