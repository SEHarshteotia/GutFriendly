package com.gutfriendly.app.dto;

import com.gutfriendly.app.model.VendorAddress;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorAddressDTO {

	private Long addressId;
	private String houseNo;
	private String street;
	private String city;
	private String state;
	private String pincode;
	private String country;

	public static VendorAddressDTO from(VendorAddress address) {
		if (address == null) {
			return null;
		}

		return new VendorAddressDTO(
				address.getAddressId(),
				address.getHouseNo(),
				address.getStreet(),
				address.getCity(),
				address.getState(),
				address.getPincode(),
				address.getCountry());
	}
}
