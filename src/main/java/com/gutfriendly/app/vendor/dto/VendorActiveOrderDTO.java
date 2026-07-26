package com.gutfriendly.app.vendor.dto;

import com.gutfriendly.app.vendor.status.VendorOrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorActiveOrderDTO {

	private Long orderId;
	private String orderNumber;
	private String itemsSummary;
	private VendorOrderStatus status;
	private String statusLabel;
	private long minutesAgo;
}
