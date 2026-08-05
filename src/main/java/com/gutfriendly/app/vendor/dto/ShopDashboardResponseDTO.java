package com.gutfriendly.app.vendor.dto;

import java.sql.Timestamp;
import java.util.List;

import com.gutfriendly.app.vendor.status.VendorStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Full dashboard payload combining vendor info, onboarding state, and shop analytics.
 * <p>
 * Used by {@code GET /vendor/{vendorId}/shops/{shopId}/dashboard} and serviceability recheck.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopDashboardResponseDTO {

	private Integer vendorId;
	private Long shopId;
	private String shopName;
	private String fullName;
	private String phoneNo;
	private String email;
	private Boolean active;
	private VendorStatus status;
	private Timestamp joiningDate;
	private ShopAddressDTO address;
	private boolean serviceableLocation;
	private int profileCompletionPercentage;
	private String nextAction;
	private List<String> pendingRequirements;
	private ShopDashboardSummaryDTO summary;
	private List<ShopActiveOrderDTO> activeOrders;
	private List<ShopTopSellingItemDTO> topSellingItems;
	private List<ShopRecentReviewDTO> recentReviews;
	private ShopStatusDTO shopStatus;
}
