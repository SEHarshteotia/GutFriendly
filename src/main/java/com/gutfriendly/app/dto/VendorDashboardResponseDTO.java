package com.gutfriendly.app.dto;

import java.sql.Timestamp;
import java.util.List;

import com.gutfriendly.app.status.VendorStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorDashboardResponseDTO {

	private Integer vendorId;
	private String fullName;
	private String phoneNo;
	private String email;
	private Boolean active;
	private VendorStatus status;
	private Timestamp joiningDate;
	private VendorAddressDTO address;
	private boolean serviceableLocation;
	private int profileCompletionPercentage;
	private String nextAction;
	private List<String> pendingRequirements;
	private VendorDashboardSummaryDTO summary;
	private List<VendorActiveOrderDTO> activeOrders;
	private List<VendorTopSellingItemDTO> topSellingItems;
	private List<VendorRecentReviewDTO> recentReviews;
	private VendorStoreStatusDTO storeStatus;
}
