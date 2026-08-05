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
public class StoreDashboardResponseDTO {

	private Integer vendorId;
	private Long shopId;
	private String shopName;
	private String fullName;
	private String phoneNo;
	private String email;
	private Boolean active;
	private VendorStatus status;
	private Timestamp joiningDate;
	private StoreAddressDTO address;
	private boolean serviceableLocation;
	private int profileCompletionPercentage;
	private String nextAction;
	private List<String> pendingRequirements;
	private StoreDashboardSummaryDTO summary;
	private List<StoreActiveOrderDTO> activeOrders;
	private List<StoreTopSellingItemDTO> topSellingItems;
	private List<StoreRecentReviewDTO> recentReviews;
	private StoreStatusDTO storeStatus;
}
