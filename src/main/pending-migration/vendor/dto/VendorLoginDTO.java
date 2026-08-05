package com.gutfriendly.app.vendor.dto;

import lombok.Data;

/**
 * Request body for vendor login.
 * <p>
 * Used by {@code POST /vendor/login}.
 * Required fields: {@code phoneNo}, {@code password}.
 */
@Data
public class VendorLoginDTO {

	private String phoneNo;
	private String password;
	
}
