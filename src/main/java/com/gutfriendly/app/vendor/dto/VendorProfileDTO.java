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
