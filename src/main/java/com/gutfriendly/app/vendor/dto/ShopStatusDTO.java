package com.gutfriendly.app.vendor.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Operational shop flags for dashboard shop-status widget.
 * <p>
 * Used by {@code GET /vendor/{vendorId}/shops/{shopId}/dashboard/shop-status}
 * and nested in {@link ShopDashboardResponseDTO}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopStatusDTO {

	private Long shopId;
	private String shopName;
	private String imageUrl;
	private boolean open;
	private LocalTime openTime;
	private boolean onlineOrdersEnabled;
	private Integer estimatedPrepTimeMinutes;
	private Double rating;
	private Long ratingCount;
}
