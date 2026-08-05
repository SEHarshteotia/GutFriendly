package com.gutfriendly.app.vendor.dto;

import com.gutfriendly.app.vendor.model.Store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Shop rating returned by the rating endpoint.
 * <p>
 * Used by {@code GET /vendor/{vendorId}/shops/{shopId}/rating}.
 * Values are stored on the shop and will be updated by a dedicated sync API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreRatingDTO {

	private Long shopId;
	private Double rating;
	private Long ratingCount;

	public static StoreRatingDTO from(Store store) {
		return new StoreRatingDTO(
				store.getStoreId(),
				store.getRating(),
				store.getRatingCount());
	}
}
