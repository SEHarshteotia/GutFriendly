package com.gutfriendly.app.vendor.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.gutfriendly.app.vendor.status.ShopOrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Order detail with line items for vendor order endpoints.
 * <p>
 * Used by order list, get, and status update endpoints under
 * {@code /vendor/{vendorId}/shops/{shopId}/orders}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopOrderDTO {

	private Long orderId;
	private String orderNumber;
	private ShopOrderStatus status;
	private String statusLabel;
	private BigDecimal totalAmount;
	private LocalDateTime createdAt;
	private long minutesAgo;
	private List<ShopOrderItemDTO> items;
}
