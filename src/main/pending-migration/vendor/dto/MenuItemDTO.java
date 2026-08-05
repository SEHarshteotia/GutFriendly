package com.gutfriendly.app.vendor.dto;

import java.math.BigDecimal;

import com.gutfriendly.app.vendor.model.MenuItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Menu item representation returned by menu CRUD endpoints.
 * <p>
 * Used by menu list, get, create, update, and toggle endpoints under
 * {@code /vendor/{vendorId}/shops/{shopId}/menu}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDTO {

	private Long itemId;
	private String name;
	private String category;
	private String description;
	private BigDecimal price;
	private String imageUrl;
	private boolean active;

	public static MenuItemDTO from(MenuItem item) {
		return new MenuItemDTO(
				item.getItemId(),
				item.getName(),
				item.getCategory(),
				item.getDescription(),
				item.getPrice(),
				item.getImageUrl(),
				Boolean.TRUE.equals(item.getActive()));
	}
}
