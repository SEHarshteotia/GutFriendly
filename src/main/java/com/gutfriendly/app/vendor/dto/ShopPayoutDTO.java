package com.gutfriendly.app.vendor.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.gutfriendly.app.vendor.model.ShopPayout;
import com.gutfriendly.app.vendor.enums.PayoutStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Single payout record for a shop.
 * <p>
 * Used in payout list responses from {@code GET /vendor/{vendorId}/shops/{shopId}/payouts}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopPayoutDTO {

	private Long payoutId;
	private BigDecimal amount;
	private PayoutStatus status;
	private LocalDate periodStart;
	private LocalDate periodEnd;
	private LocalDateTime paidAt;
	private String referenceNumber;
	private String description;
	private LocalDateTime createdAt;

	public static ShopPayoutDTO from(ShopPayout payout) {
		return new ShopPayoutDTO(
				payout.getPayoutId(),
				payout.getAmount(),
				payout.getStatus(),
				payout.getPeriodStart(),
				payout.getPeriodEnd(),
				payout.getPaidAt(),
				payout.getReferenceNumber(),
				payout.getDescription(),
				payout.getCreatedAt());
	}
}
