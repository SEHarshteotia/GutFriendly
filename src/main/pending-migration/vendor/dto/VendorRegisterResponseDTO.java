package com.gutfriendly.app.vendor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response after successful vendor registration.
 * <p>
 * Used by {@code POST /vendor/register}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorRegisterResponseDTO {

	private Integer vendorId;
	private String message;
}
