package com.gutfriendly.app.vendor.dto;

import java.time.LocalDateTime;

import com.gutfriendly.app.vendor.model.StoreReview;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Customer review with optional vendor reply.
 * <p>
 * Used by review list, reply, and nested dashboard responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreReviewDTO {

	private Long reviewId;
	private String customerName;
	private String customerImageUrl;
	private Integer rating;
	private String comment;
	private String vendorReply;
	private LocalDateTime repliedAt;
	private LocalDateTime createdAt;
	private long minutesAgo;

	public static StoreReviewDTO from(StoreReview review, long minutesAgo) {
		return new StoreReviewDTO(
				review.getReviewId(),
				review.getCustomerName(),
				review.getCustomerImageUrl(),
				review.getRating(),
				review.getComment(),
				review.getVendorReply(),
				review.getRepliedAt(),
				review.getCreatedAt(),
				minutesAgo);
	}
}
