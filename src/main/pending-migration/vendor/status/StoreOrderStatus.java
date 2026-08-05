package com.gutfriendly.app.vendor.status;

/**
 * Lifecycle status of a customer order from the vendor's perspective.
 */
public enum StoreOrderStatus {
	/** Newly placed, not yet started. */
	NEW,
	/** Food is being prepared. */
	PREPARING,
	/** Order is with delivery partner or en route. */
	OUT_FOR_DELIVERY,
	/** Order completed successfully. */
	DELIVERED,
	/** Order was cancelled. */
	CANCELLED
}
