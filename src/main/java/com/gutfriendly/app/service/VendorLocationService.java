package com.gutfriendly.app.service;

import org.springframework.stereotype.Service;

import com.gutfriendly.app.dto.VendorLocationRequestDTO;
import com.gutfriendly.app.dto.VendorLocationResponseDTO;
import com.gutfriendly.app.model.VendorAddress;
import com.gutfriendly.app.model.VendorDetails;
import com.gutfriendly.app.repository.ServiceableAreaRepo;
import com.gutfriendly.app.repository.VendorAddressRepo;
import com.gutfriendly.app.repository.VendorRepo;
import com.gutfriendly.app.status.VendorStatus;

@Service
public class VendorLocationService {

	private final VendorRepo vendorRepository;
	private final VendorAddressRepo addressRepository;
	private final ServiceableAreaRepo areaRepository;

	VendorLocationService(VendorRepo vendorRepository, VendorAddressRepo addressRepository,
			ServiceableAreaRepo areaRepository) {
		this.vendorRepository = vendorRepository;
		this.addressRepository = addressRepository;
		this.areaRepository = areaRepository;
	}

	public VendorLocationResponseDTO saveLocation(VendorLocationRequestDTO request) {

		VendorDetails vendor = vendorRepository.findById(request.getVendorId())
				.orElseThrow(() -> new RuntimeException("Vendor not found"));

		VendorAddress address = addressRepository.findByVendor(vendor).orElse(new VendorAddress());

		address.setVendor(vendor);
		address.setHouseNo(request.getHouseNo());
		address.setStreet(request.getStreet());
		address.setCity(request.getCity());
		address.setState(request.getState());
		address.setPincode(request.getPincode());

		addressRepository.save(address);

		if (areaRepository.existsByPincode(request.getPincode())) {

			vendor.setStatus(VendorStatus.UNDER_REVIEW);
			vendorRepository.save(vendor);

			return new VendorLocationResponseDTO(true, VendorStatus.UNDER_REVIEW,
					"Your location is serviceable. Your account has been sent for review.");
		} else {

			vendor.setStatus(VendorStatus.NOT_SERVICEABLE);
			vendorRepository.save(vendor);

			return new VendorLocationResponseDTO(false, VendorStatus.NOT_SERVICEABLE,
					"Sorry! We are currently not operating in your area.");
		}

	}

}
