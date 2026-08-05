package com.gutfriendly.app.vendor.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response wrapper for a list of menu items.
 * <p>
 * Used by {@code GET /vendor/{vendorId}/shops/{shopId}/menu}.
 * Wraps {@link MenuItemDTO} entries in {@code items}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemListResponseDTO {

	private List<MenuItemDTO> items;
}
