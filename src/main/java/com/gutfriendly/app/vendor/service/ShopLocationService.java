package com.gutfriendly.app.vendor.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.gutfriendly.app.admin.model.Pincode;
import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.admin.model.VendorDetails;
import com.gutfriendly.app.admin.model.VendorShopAddress;
import com.gutfriendly.app.admin.repository.PincodeRepository;
import com.gutfriendly.app.admin.repository.ShopDetailsRepository;
import com.gutfriendly.app.admin.repository.VendorDetailsRepository;
import com.gutfriendly.app.admin.repository.VendorShopAddressRepository;
import com.gutfriendly.app.vendor.dto.ShopLocationRequestDTO;
import com.gutfriendly.app.vendor.dto.ShopLocationResponseDTO;
import com.gutfriendly.app.vendor.mapper.AddressMapper;
import com.gutfriendly.app.vendor.mapper.ShopStatusMapper;
import com.gutfriendly.app.vendor.status.VendorStatus;

@Service
public class ShopLocationService {

	private final VendorDetailsRepository vendorRepository;
	private final ShopDetailsRepository shopRepository;
	private final VendorShopAddressRepository addressRepository;
	private final PincodeRepository pincodeRepository;

	ShopLocationService(VendorDetailsRepository vendorRepository, ShopDetailsRepository shopRepository,
			VendorShopAddressRepository addressRepository, PincodeRepository pincodeRepository) {
		this.vendorRepository = vendorRepository;
		this.shopRepository = shopRepository;
		this.addressRepository = addressRepository;
		this.pincodeRepository = pincodeRepository;
	}

	@Transactional
	public ShopLocationResponseDTO saveLocation(ShopLocationRequestDTO request) {
		VendorDetails vendor = vendorRepository.findById(request.getVendorId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found"));

		if (request.getShopId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop ID is required");
		}
		if (request.getPincode() == null || request.getPincode().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pincode is required");
		}

		ShopDetails shop = shopRepository.findByShopIdAndVendor(request.getShopId().intValue(), vendor)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found"));

		Pincode pincode = resolvePincode(request);
		VendorShopAddress address = shop.getAddress_id() != null ? shop.getAddress_id() : new VendorShopAddress();
		AddressMapper.applyRequest(address, request, pincode);
		VendorShopAddress savedAddress = addressRepository.save(address);
		shop.setAddress_id(savedAddress);

		ShopStatusMapper.applyServiceabilityResult(shop, true);
		shopRepository.save(shop);

		VendorStatus status = ShopStatusMapper.toVendorStatus(shop);
		return new ShopLocationResponseDTO(true, status,
				"Your shop location is serviceable. Book an inspection to proceed.");
	}

	private Pincode resolvePincode(ShopLocationRequestDTO request) {
		String pin = request.getPincode().trim();
		return pincodeRepository.findById(pin)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Pincode is not serviceable. We do not operate in this area."));
	}
}
