package com.gutfriendly.app.vendor.mapper;

import com.gutfriendly.app.admin.model.Pincode;
import com.gutfriendly.app.admin.model.VendorShopAddress;
import com.gutfriendly.app.vendor.dto.ShopAddressDTO;
import com.gutfriendly.app.vendor.dto.ShopLocationRequestDTO;

public final class AddressMapper {

	private AddressMapper() {
	}

	public static ShopAddressDTO toDto(VendorShopAddress address) {
		if (address == null) {
			return null;
		}

		Pincode pincode = address.getPinCode();
		return new ShopAddressDTO(
				(long) address.getAddressId(),
				address.getShopNumber(),
				address.getLocality(),
				pincode != null ? pincode.getCity() : null,
				pincode != null ? pincode.getState() : null,
				pincode != null ? pincode.getPin_code() : null,
				"India");
	}

	public static void applyRequest(VendorShopAddress address, ShopLocationRequestDTO request, Pincode pincode) {
		String locality = joinNonBlank(request.getStreet(), request.getCity());
		if (locality.isBlank()) {
			locality = request.getCity();
		}
		address.setShopNumber(request.getHouseNo() != null ? request.getHouseNo().trim() : "");
		address.setLocality(locality != null ? locality.trim() : "");
		address.setPinCode(pincode);
	}

	private static String joinNonBlank(String first, String second) {
		if (first == null || first.isBlank()) {
			return second != null ? second : "";
		}
		if (second == null || second.isBlank()) {
			return first;
		}
		return first.trim() + ", " + second.trim();
	}
}
