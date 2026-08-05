package com.gutfriendly.app.vendor.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response wrapper for all shop reviews.
 * <p>
 * Used by {@code GET /vendor/{vendorId}/shops/{shopId}/reviews}.
 * Wraps {@link StoreReviewDTO} entries in {@code reviews}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreReviewListResponseDTO {

	private List<StoreReviewDTO> reviews;
}
