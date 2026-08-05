package com.gutfriendly.app.vendor.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Aggregate payout balances and completed payout count for a shop.
 * <p>
 * Used by {@code GET /vendor/{vendorId}/shops/{shopId}/payouts/summary}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopPayoutSummaryDTO {

	private BigDecimal pendingBalance;
	private BigDecimal totalEarned;
	private BigDecimal totalPaidOut;
	private long completedPayouts;
}
