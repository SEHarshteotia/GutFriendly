package com.gutfriendly.app.vendor.dto;

import java.time.LocalTime;

import lombok.Data;

/**
 * Request body for updating shop or store settings.
 * <p>
 * Used by {@code PUT /vendor/{vendorId}/shops/{shopId}} and
 * {@code PUT /vendor/{vendorId}/shops/{shopId}/store}.
 * All fields are optional; only provided fields are updated.
 */
@Data
public class UpdateShopRequestDTO {

	private String storeName;
	private String imageUrl;
	private Boolean isOpen;
	private LocalTime openTime;
	private LocalTime closeTime;
	private Boolean onlineOrdersEnabled;
	private Integer estimatedPrepTimeMinutes;
}
