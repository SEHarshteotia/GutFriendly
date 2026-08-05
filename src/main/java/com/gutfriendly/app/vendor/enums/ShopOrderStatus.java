package com.gutfriendly.app.vendor.enums;

/**
 * Lifecycle status of a customer order from the vendor's perspective.
 */
public enum ShopOrderStatus {
	/** Newly placed, not yet started. */
	NEW,
	/** Vendor accepted the order. */
	ACCEPTED,
	/** Food is being prepared. */
	PREPARING,
	/** Order is with delivery partner or en route. */
	OUT_FOR_DELIVERY,
	/** Order completed successfully. */
	DELIVERED,
	/** Order was cancelled. */
	CANCELLED
}
