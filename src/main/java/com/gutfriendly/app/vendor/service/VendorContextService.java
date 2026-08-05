package com.gutfriendly.app.vendor.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.admin.model.VendorDetails;
import com.gutfriendly.app.admin.repository.ShopDetailsRepository;
import com.gutfriendly.app.admin.repository.VendorDetailsRepository;

@Service
public class VendorContextService {

	private final VendorDetailsRepository vendorRepository;
	private final ShopDetailsRepository shopRepository;

	VendorContextService(VendorDetailsRepository vendorRepository, ShopDetailsRepository shopRepository) {
		this.vendorRepository = vendorRepository;
		this.shopRepository = shopRepository;
	}

	public VendorDetails findVendor(Integer vendorId) {
		return vendorRepository.findById(vendorId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found"));
	}

	public ShopDetails findShop(Integer vendorId, Long shopId) {
		VendorDetails vendor = findVendor(vendorId);
		return shopRepository.findByShopIdAndVendor(shopId.intValue(), vendor)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found"));
	}
}
