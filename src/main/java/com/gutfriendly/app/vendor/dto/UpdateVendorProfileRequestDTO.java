package com.gutfriendly.app.vendor.dto;

import lombok.Data;

/**
 * Request body for updating vendor profile fields.
 * <p>
 * Used by {@code PUT /vendor/{vendorId}/settings/profile}.
 * All fields are optional; only provided fields are updated.
 */
@Data
public class UpdateVendorProfileRequestDTO {

	private String fName;
	private String mName;
	private String lName;
	private String email;
	private String aadharNo;
	private String panNo;
}
