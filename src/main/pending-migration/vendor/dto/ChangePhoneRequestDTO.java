package com.gutfriendly.app.vendor.dto;

import lombok.Data;

/**
 * Request body for changing the vendor's phone number.
 * <p>
 * Used by {@code POST /vendor/{vendorId}/settings/change-phone}.
 * Requires the current password to confirm identity.
 */
@Data
public class ChangePhoneRequestDTO {

	private String newPhoneNo;
	private String password;
}
