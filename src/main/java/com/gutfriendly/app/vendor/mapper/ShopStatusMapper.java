package com.gutfriendly.app.vendor.mapper;

import com.gutfriendly.app.admin.enums.ServiceAvailabilityStatus;
import com.gutfriendly.app.admin.enums.ShopStatus;
import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.vendor.status.VendorStatus;

public final class ShopStatusMapper {

	private ShopStatusMapper() {
	}

	public static VendorStatus toVendorStatus(ShopDetails shop) {
		if (Boolean.TRUE.equals(shop.getBlocked())) {
			return VendorStatus.SUSPENDED;
		}
		if (shop.getServiceAvailabilityStatus() == ServiceAvailabilityStatus.NOT_SERVICEABLE) {
			return VendorStatus.NOT_SERVICEABLE;
		}
		if (shop.getStatus() == ShopStatus.REJECTED) {
			return VendorStatus.REJECTED;
		}
		if (shop.getStatus() == ShopStatus.VERIFIED) {
			return VendorStatus.APPROVED;
		}
		if (shop.getServiceAvailabilityStatus() == ServiceAvailabilityStatus.SERVICEABLE) {
			return VendorStatus.UNDER_REVIEW;
		}
		return VendorStatus.PENDING;
	}

	public static void applyServiceabilityResult(ShopDetails shop, boolean serviceable) {
		if (!serviceable) {
			shop.setServiceAvailabilityStatus(ServiceAvailabilityStatus.NOT_SERVICEABLE);
			return;
		}
		shop.setServiceAvailabilityStatus(ServiceAvailabilityStatus.SERVICEABLE);
	}

	public static boolean isServiceableStatus(VendorStatus status) {
		return status == VendorStatus.SERVICEABLE
				|| status == VendorStatus.UNDER_REVIEW
				|| status == VendorStatus.APPROVED;
	}

	public static VendorStatus statusAfterServiceabilityCheck(VendorStatus currentStatus, boolean serviceableLocation) {
		if (!serviceableLocation) {
			return VendorStatus.NOT_SERVICEABLE;
		}
		if (currentStatus == VendorStatus.NOT_SERVICEABLE || currentStatus == VendorStatus.PENDING) {
			return VendorStatus.UNDER_REVIEW;
		}
		return currentStatus;
	}
}
