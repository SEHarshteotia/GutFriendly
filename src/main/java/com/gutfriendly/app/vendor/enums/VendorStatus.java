package com.gutfriendly.app.vendor.enums;

/**
 * Onboarding and operational approval status for a vendor or shop.
 */
public enum VendorStatus {
	/** Initial state after registration or shop creation. */
	PENDING,
	/** Location is within a serviceable pincode. */
	SERVICEABLE,
	/** Location pincode is outside serviceable areas. */
	NOT_SERVICEABLE,
	/** Admin has approved the shop for operations. */
	APPROVED,
	/** Admin rejected the shop application. */
	REJECTED,
	/** Shop operations temporarily suspended. */
	SUSPENDED,
	/** Shop submitted for admin review after serviceability check. */
	UNDER_REVIEW
}
