package com.gutfriendly.app.vendor.enums;

/**
 * Settlement status of a vendor payout record.
 */
public enum PayoutStatus {
	/** Awaiting processing or transfer. */
	PENDING,
	/** Transfer in progress. */
	PROCESSING,
	/** Successfully paid to the vendor. */
	COMPLETED,
	/** Payment attempt failed. */
	FAILED
}
