package com.gutfriendly.app.vendor.dto;

import com.gutfriendly.app.vendor.model.StoreAddress;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Shop address representation nested in shop, store, and dashboard responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreAddressDTO {

	private Long addressId;
	private String houseNo;
	private String street;
	private String city;
	private String state;
	private String pincode;
	private String country;

	public static StoreAddressDTO from(StoreAddress address) {
		if (address == null) {
			return null;
		}

		return new StoreAddressDTO(
				address.getAddressId(),
				address.getHouseNo(),
				address.getStreet(),
				address.getCity(),
				address.getState(),
				address.getPincode(),
				address.getCountry());
	}
}
