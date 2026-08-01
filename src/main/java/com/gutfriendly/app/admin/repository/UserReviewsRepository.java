package com.gutfriendly.app.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;

import com.gutfriendly.app.admin.model.UserReviews;

public interface UserReviewsRepository extends JpaRepository<UserReviews, Integer> {
	
	@Query("Select AVG(u.overall_rating) from UserReviews u ")
	Double getAverageUserRating() ;
	
	Page<UserReviews> findAllByOrderByCreatedAtDesc(Pageable pageable);
	

}
