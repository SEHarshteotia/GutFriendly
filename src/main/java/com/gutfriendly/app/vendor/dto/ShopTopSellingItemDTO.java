package com.gutfriendly.app.vendor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ranked top-selling menu item for today's sales.
 * <p>
 * Nested in {@link ShopDashboardResponseDTO} and {@link ShopTopSellingItemListResponseDTO}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopTopSellingItemDTO {

	private int rank;
	private String itemName;
	private long quantitySold;
}
