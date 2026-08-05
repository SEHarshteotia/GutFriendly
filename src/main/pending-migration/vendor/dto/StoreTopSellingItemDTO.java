package com.gutfriendly.app.vendor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ranked top-selling menu item for today's sales.
 * <p>
 * Nested in {@link StoreDashboardResponseDTO} and {@link StoreTopSellingItemListResponseDTO}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreTopSellingItemDTO {

	private int rank;
	private String itemName;
	private long quantitySold;
}
