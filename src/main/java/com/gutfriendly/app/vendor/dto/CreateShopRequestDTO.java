package com.gutfriendly.app.vendor.dto;

import java.time.LocalTime;

import lombok.Data;

/**
 * Request body for creating a new shop.
 * <p>
 * Used by {@code POST /vendor/{vendorId}/shops}.
 * Required field: {@code shopName}.
 */
@Data
public class CreateShopRequestDTO {

	private String shopName;
	private String imageUrl;
	private LocalTime openTime;
	private Integer estimatedPrepTimeMinutes;
}
