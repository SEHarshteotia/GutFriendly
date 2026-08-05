package com.gutfriendly.app.vendor.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response wrapper for payout history.
 * <p>
 * Used by {@code GET /vendor/{vendorId}/shops/{shopId}/payouts}.
 * Wraps {@link ShopPayoutDTO} entries in {@code payouts}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopPayoutListResponseDTO {

	private List<ShopPayoutDTO> payouts;
}
