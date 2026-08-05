package com.gutfriendly.app.vendor.dto;

import lombok.Data;

/**
 * Request body for changing the vendor password.
 * <p>
 * Used by {@code POST /vendor/{vendorId}/settings/change-password}.
 * Required fields: {@code currentPassword}, {@code newPassword} (minimum 6 characters).
 */
@Data
public class ChangePasswordRequestDTO {

	private String currentPassword;
	private String newPassword;
}
