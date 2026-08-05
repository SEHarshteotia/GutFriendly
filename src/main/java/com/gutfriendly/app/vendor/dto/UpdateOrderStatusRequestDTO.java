package com.gutfriendly.app.vendor.dto;

import com.gutfriendly.app.vendor.enums.ShopOrderStatus;

import lombok.Data;

/**
 * Request body for updating an order's status.
 * <p>
 * Used by {@code PATCH /vendor/{vendorId}/shops/{shopId}/orders/{orderId}/status}.
 * Required field: {@code status}.
 */
@Data
public class UpdateOrderStatusRequestDTO {

	private ShopOrderStatus status;
}
