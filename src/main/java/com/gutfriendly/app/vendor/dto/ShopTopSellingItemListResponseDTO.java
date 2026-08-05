package com.gutfriendly.app.vendor.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response wrapper for top-selling items on the dashboard.
 * <p>
 * Used by {@code GET /vendor/{vendorId}/shops/{shopId}/dashboard/top-selling-items}.
 * Wraps {@link ShopTopSellingItemDTO} entries in {@code items}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopTopSellingItemListResponseDTO {

	private List<ShopTopSellingItemDTO> items;
}
