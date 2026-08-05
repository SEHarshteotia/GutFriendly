package com.gutfriendly.app.vendor.dto;

import com.gutfriendly.app.vendor.enums.ShopOrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Summary of an active order for dashboard display.
 * <p>
 * Nested in {@link ShopDashboardResponseDTO} and {@link ShopActiveOrderListResponseDTO}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopActiveOrderDTO {

	private Long orderId;
	private String orderNumber;
	private String itemsSummary;
	private ShopOrderStatus status;
	private String statusLabel;
	private long minutesAgo;
}
