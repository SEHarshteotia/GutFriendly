package com.gutfriendly.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorRecentReviewDTO {

	private Long reviewId;
	private String customerName;
	private String customerImageUrl;
	private Integer rating;
	private String comment;
	private long minutesAgo;
}
