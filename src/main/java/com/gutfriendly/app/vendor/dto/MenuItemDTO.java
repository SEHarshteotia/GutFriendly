package com.gutfriendly.app.vendor.dto;

import java.math.BigDecimal;

import com.gutfriendly.app.admin.model.FoodItemsDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

	public static MenuItemDTO from(FoodItemsDetails item) {
		return new MenuItemDTO(
				(long) item.getFoodId(),
				item.getFoodName(),
				item.getFoodCategory(),
				item.getFoodDesc(),
				item.getPrice(),
				null,
				item.isAvailable());
	}
}
