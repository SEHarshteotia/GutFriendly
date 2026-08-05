package com.gutfriendly.app.vendor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response for active order count queries.
 * <p>
 * Used by {@code GET /vendor/{vendorId}/shops/{shopId}/orders/active-count}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveOrderCountResponseDTO {

	private long count;
}
