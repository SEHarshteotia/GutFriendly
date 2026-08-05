package com.gutfriendly.app.vendor.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Single hourly data point for the dashboard order overview chart.
 * <p>
 * Nested inside {@link OrderOverviewResponseDTO}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderOverviewPointDTO {

	private int hour;
	private String label;
	private long orders;
	private BigDecimal revenue;
}
