package com.gutfriendly.app.vendor.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response wrapper for a list of orders.
 * <p>
 * Used by {@code GET /vendor/{vendorId}/shops/{shopId}/orders}.
 * Wraps {@link StoreOrderDTO} entries in {@code orders}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreOrderListResponseDTO {

	private List<StoreOrderDTO> orders;
}
