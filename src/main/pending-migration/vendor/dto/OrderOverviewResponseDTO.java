package com.gutfriendly.app.vendor.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response wrapping hourly order overview data for the current day.
 * <p>
 * Used by {@code GET /vendor/{vendorId}/shops/{shopId}/dashboard/order-overview}.
 * Wraps {@link OrderOverviewPointDTO} entries in {@code points}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderOverviewResponseDTO {

	private List<OrderOverviewPointDTO> points;
}
