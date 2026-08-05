package com.gutfriendly.app.vendor.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response after successful vendor login.
 * <p>
 * Used by {@code POST /vendor/login}.
 * Includes vendor profile and list of shops.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorLoginResponseDTO {

	private String message;
	private VendorProfileDTO vendor;
	private List<StoreDTO> shops;
}
