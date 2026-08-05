package com.gutfriendly.app.vendor.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response wrapper for recent reviews on the dashboard.
 * <p>
 * Used by {@code GET /vendor/{vendorId}/shops/{shopId}/dashboard/recent-reviews}.
 * Wraps {@link ShopRecentReviewDTO} entries in {@code reviews}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopRecentReviewListResponseDTO {

	private List<ShopRecentReviewDTO> reviews;
}
