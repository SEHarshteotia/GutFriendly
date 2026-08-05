package com.gutfriendly.app.vendor.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * Request body for partial menu item updates.
 * <p>
 * Used by {@code PUT /vendor/{vendorId}/shops/{shopId}/menu/{itemId}}.
 * All fields are optional; only provided fields are updated.
 */
@Data
public class UpdateMenuItemRequestDTO {

	private String name;
	private String category;
	private String description;
	private BigDecimal price;
	private String imageUrl;
	private Boolean active;
}
