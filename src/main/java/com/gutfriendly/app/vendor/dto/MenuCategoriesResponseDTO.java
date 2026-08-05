package com.gutfriendly.app.vendor.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response wrapping valid menu item category enum names.
 * <p>
 * Used by {@code GET /vendor/{vendorId}/shops/{shopId}/menu/categories}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuCategoriesResponseDTO {

	private List<String> categories;
}
