package com.gutfriendly.app.vendor.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Today's order and revenue summary with day-over-day change percentages.
 * <p>
 * Used by {@code GET /vendor/{vendorId}/shops/{shopId}/dashboard/summary}
 * and nested in {@link StoreDashboardResponseDTO}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreDashboardSummaryDTO {

	private long todaysOrders;
	private BigDecimal todaysRevenue;
	private BigDecimal averageOrderValue;
	private double averageRating;
	private long reviewCount;
	private double ordersChangePercent;
	private double revenueChangePercent;
	private double avgOrderValueChangePercent;
}
