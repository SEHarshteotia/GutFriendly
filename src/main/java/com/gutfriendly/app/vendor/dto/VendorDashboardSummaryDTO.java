package com.gutfriendly.app.vendor.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorDashboardSummaryDTO {

	private long todaysOrders;
	private BigDecimal todaysRevenue;
	private BigDecimal averageOrderValue;
	private double averageRating;
	private long reviewCount;
}
