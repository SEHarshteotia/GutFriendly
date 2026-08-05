package com.gutfriendly.app.vendor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Compact customer review for dashboard recent-reviews widget.
 * <p>
 * Nested in {@link ShopDashboardResponseDTO} and {@link ShopRecentReviewListResponseDTO}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopRecentReviewDTO {

	private Long reviewId;
	private String customerName;
	private String customerImageUrl;
	private Integer rating;
	private String comment;
	private long minutesAgo;
}
