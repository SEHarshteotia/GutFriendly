package com.gutfriendly.app.vendor.dto;

import java.sql.Timestamp;

import com.gutfriendly.app.vendor.model.VendorDetails;
import com.gutfriendly.app.vendor.status.VendorStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Vendor account profile returned by login and settings endpoints.
 * <p>
 * Used by {@code GET/PUT /vendor/{vendorId}/settings/profile} and login response.
 */
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

	public static VendorProfileDTO from(VendorDetails vendor) {
		return new VendorProfileDTO(
				vendor.getVendor_id(),
				vendor.getFName(),
				vendor.getMName(),
				vendor.getLName(),
				vendor.getPhoneNo(),
				vendor.getEmail(),
				vendor.getAadharNo(),
				vendor.getPanNo(),
				vendor.getIsActive(),
				vendor.getStatus(),
				vendor.getJoining_date());
	}
}
