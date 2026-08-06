package com.gutfriendly.app.vendor.dto;

import lombok.Data;

/**
 * Request body for vendor self-registration.
 * <p>
 * Used by {@code POST /vendor/register}.
 * Required fields: {@code fName}, {@code lName}, {@code phoneNo}, {@code password}.
 * Optional fields: {@code mName}, {@code email}, {@code aadharNo}, {@code panNo}.
 */
@Data
public class VendorRegisterRequestDTO {

	private String fName;
	private String mName;
	private String lName;
	private String phoneNo;
	private String password;
	private String email;
	private String aadharNo;
	private String panNo;
}
