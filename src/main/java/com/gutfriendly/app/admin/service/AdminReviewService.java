package com.gutfriendly.app.admin.service;


import org.springframework.data.domain.Page;

import com.gutfriendly.app.admin.dto.response.AdminReviewSummaryDTO;
import com.gutfriendly.app.reviews.dto.ReviewDTO;

public interface AdminReviewService {
	



		/**
		 * Lists reviews across the platform (or scoped to a single shop) for the
		 * admin review moderation page. Only active reviews are included by
		 * default.
		 */
		Page<ReviewDTO> getAllReviews(
				int page,
				int size,
				String sortBy,
				String direction,
				Integer shopId,
				Integer rating,
				String shopName,
				boolean includeInactive);

		ReviewDTO getReviewById(int reviewId);

		AdminReviewSummaryDTO getReviewsSummary();

		/**
		 * Hides ({@code active=false}) or restores ({@code active=true}) a review
		 * from public/vendor visibility and recalculates the affected shop's
		 * trust score.
		 */
		ReviewDTO moderateReview(int reviewId, boolean active, String reason);
	}



