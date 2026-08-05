package com.gutfriendly.app.vendor.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response wrapper for active orders on the dashboard.
 * <p>
 * Used by {@code GET /vendor/{vendorId}/shops/{shopId}/dashboard/active-orders}.
 * Wraps {@link ShopActiveOrderDTO} entries in {@code orders}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopActiveOrderListResponseDTO {

	private List<ShopActiveOrderDTO> orders;
}
