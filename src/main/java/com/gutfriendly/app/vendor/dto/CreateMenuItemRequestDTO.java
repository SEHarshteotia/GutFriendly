package com.gutfriendly.app.vendor.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * Request body for creating a menu item.
 * <p>
 * Used by {@code POST /vendor/{vendorId}/shops/{shopId}/menu}.
 * Required fields: {@code name}, {@code price} (positive), {@code category} ({@link com.gutfriendly.app.vendor.enums.MenuItemCategory} name).
 */
@Data
public class CreateMenuItemRequestDTO {

	private String name;
	private String category;
	private String description;
	private BigDecimal price;
	private String imageUrl;
}
