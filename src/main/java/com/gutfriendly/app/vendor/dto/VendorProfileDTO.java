package com.gutfriendly.app.vendor.dto;

import java.sql.Timestamp;

import com.gutfriendly.app.admin.model.VendorDetails;
import com.gutfriendly.app.vendor.enums.VendorStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorProfileDTO {

	private Integer vendorId;
	private String fName;
	private String mName;
	private String lName;
	private String phoneNo;
	private String email;
	private String aadharNo;
	private String panNo;
	private Boolean active;
	private VendorStatus status;
	private Timestamp joiningDate;

	/**
	 * Same as {@link #from(VendorDetails)} but with the identity documents
	 * reduced to their last four characters. Used by the login response, which
	 * had been returning full Aadhaar and PAN numbers in cleartext.
	 */
	public static VendorProfileDTO masked(VendorDetails vendor) {
		VendorProfileDTO dto = from(vendor);
		dto.setAadharNo(maskTail(dto.getAadharNo()));
		dto.setPanNo(maskTail(dto.getPanNo()));
		return dto;
	}

	private static String maskTail(String value) {
		if (value == null || value.isBlank()) {
			return value;
		}
		String trimmed = value.trim();
		if (trimmed.length() <= 4) {
			return "*".repeat(trimmed.length());
		}
		return "*".repeat(trimmed.length() - 4)
				+ trimmed.substring(trimmed.length() - 4);
	}

	public static VendorProfileDTO from(VendorDetails vendor) {
		Timestamp joiningDate = vendor.getJoiningDate() != null
				? Timestamp.valueOf(vendor.getJoiningDate())
				: null;
		VendorStatus status = vendor.isActive() ? VendorStatus.APPROVED : VendorStatus.SUSPENDED;

		return new VendorProfileDTO(
				vendor.getVendorId(),
				vendor.getFirstName(),
				vendor.getMiddleName(),
				vendor.getLastName(),
				vendor.getPhoneNo(),
				vendor.getEmail(),
				vendor.getAdharNo(),
				vendor.getPanNo(),
				vendor.isActive(),
				status,
				joiningDate);
	}
}
