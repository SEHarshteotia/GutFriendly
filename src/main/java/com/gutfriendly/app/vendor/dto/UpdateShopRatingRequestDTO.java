package com.gutfriendly.app.vendor.dto;

import lombok.Data;

/**
 * Request body for updating a shop's cached rating.
 * <p>
 * Intended for use by the rating sync API once implemented.
 */
@Data
public class UpdateShopRatingRequestDTO {

	private Double rating;
	private Long ratingCount;
}
