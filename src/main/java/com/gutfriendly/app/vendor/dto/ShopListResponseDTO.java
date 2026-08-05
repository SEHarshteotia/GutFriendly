package com.gutfriendly.app.vendor.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response wrapper for a vendor's shop list.
 * <p>
 * Used by {@code GET /vendor/{vendorId}/shops}.
 * Wraps {@link ShopDTO} entries in {@code shops}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopListResponseDTO {

	private List<ShopDTO> shops;
}
