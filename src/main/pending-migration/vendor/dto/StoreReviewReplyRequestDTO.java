package com.gutfriendly.app.vendor.dto;

import lombok.Data;

/**
 * Request body for posting a vendor reply to a review.
 * <p>
 * Used by {@code POST /vendor/{vendorId}/shops/{shopId}/reviews/{reviewId}/reply}.
 * Required field: {@code reply}.
 */
@Data
public class StoreReviewReplyRequestDTO {

	private String reply;
}
