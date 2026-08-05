package com.gutfriendly.app.vendor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Aggregate review statistics including average rating and per-star counts.
 * <p>
 * Used by {@code GET /vendor/{vendorId}/shops/{shopId}/reviews/stats}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreReviewStatsDTO {

	private double averageRating;
	private long totalReviews;
	private long fiveStarCount;
	private long fourStarCount;
	private long threeStarCount;
	private long twoStarCount;
	private long oneStarCount;
}
