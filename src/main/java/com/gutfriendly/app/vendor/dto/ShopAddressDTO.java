package com.gutfriendly.app.vendor.dto;

import com.gutfriendly.app.admin.model.VendorShopAddress;
import com.gutfriendly.app.vendor.mapper.AddressMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopAddressDTO {

	private Long addressId;
	private String houseNo;
	private String street;
	private String city;
	private String state;
	private String pincode;
	private String country;

	public static ShopAddressDTO from(VendorShopAddress address) {
		return AddressMapper.toDto(address);
	}
}
