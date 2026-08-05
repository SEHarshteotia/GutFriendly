package com.gutfriendly.app.vendor.dto;

import java.util.List;

import com.gutfriendly.app.reviews.dto.ReviewDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response wrapper for read-only shop reviews.
 * <p>
 * Used by {@code GET /vendor/{vendorId}/shops/{shopId}/reviews}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopReviewListResponseDTO {

	private List<ReviewDTO> reviews;
}
