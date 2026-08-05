package com.gutfriendly.app.vendor.dto;

import com.gutfriendly.app.vendor.status.StoreOrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Summary of an active order for dashboard display.
 * <p>
 * Nested in {@link StoreDashboardResponseDTO} and {@link StoreActiveOrderListResponseDTO}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreActiveOrderDTO {

	private Long orderId;
	private String orderNumber;
	private String itemsSummary;
	private StoreOrderStatus status;
	private String statusLabel;
	private long minutesAgo;
}
