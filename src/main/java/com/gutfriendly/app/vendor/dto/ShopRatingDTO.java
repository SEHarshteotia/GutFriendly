package com.gutfriendly.app.vendor.dto;

import com.gutfriendly.app.admin.model.ShopDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopRatingDTO {

	private Long shopId;
	private Double rating;
	private Long ratingCount;

	public static ShopRatingDTO from(ShopDetails shop) {
		return new ShopRatingDTO(
				(long) shop.getShopId(),
				shop.getRating(),
				shop.getRatingCount() != null ? shop.getRatingCount() : 0L);
	}
}
