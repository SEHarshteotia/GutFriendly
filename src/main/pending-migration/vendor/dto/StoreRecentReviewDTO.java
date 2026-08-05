package com.gutfriendly.app.vendor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Compact customer review for dashboard recent-reviews widget.
 * <p>
 * Nested in {@link StoreDashboardResponseDTO} and {@link StoreRecentReviewListResponseDTO}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreRecentReviewDTO {

	private Long reviewId;
	private String customerName;
	private String customerImageUrl;
	private Integer rating;
	private String comment;
	private long minutesAgo;
}
