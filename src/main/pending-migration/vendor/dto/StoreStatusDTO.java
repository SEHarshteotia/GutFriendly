package com.gutfriendly.app.vendor.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Operational store flags for dashboard store-status widget.
 * <p>
 * Used by {@code GET /vendor/{vendorId}/shops/{shopId}/dashboard/store-status}
 * and nested in {@link StoreDashboardResponseDTO}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreStatusDTO {

	private Long shopId;
	private String storeName;
	private String imageUrl;
	private boolean open;
	private LocalTime openTime;
	private boolean onlineOrdersEnabled;
	private Integer estimatedPrepTimeMinutes;
	private Double rating;
	private Long ratingCount;
}
