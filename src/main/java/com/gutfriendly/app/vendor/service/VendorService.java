package com.gutfriendly.app.vendor.service;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gutfriendly.app.admin.model.VendorDetails;
import com.gutfriendly.app.admin.repository.VendorDetailsRepository;
import com.gutfriendly.app.vendor.dto.VendorLoginDTO;
import com.gutfriendly.app.vendor.dto.VendorLoginResponseDTO;
import com.gutfriendly.app.vendor.dto.VendorProfileDTO;
import com.gutfriendly.app.vendor.dto.VendorRegisterRequestDTO;
import com.gutfriendly.app.vendor.dto.VendorRegisterResponseDTO;
import com.gutfriendly.app.vendor.util.PhoneNumberUtil;

@Service
public class VendorService {

	private final VendorDetailsRepository repo;
	private final VendorShopService vendorShopService;

	VendorService(VendorDetailsRepository repo, VendorShopService vendorShopService) {
		this.repo = repo;
		this.vendorShopService = vendorShopService;
	}

	public VendorRegisterResponseDTO register(VendorRegisterRequestDTO request) {
		validateRegistration(request);

		VendorDetails vendor = new VendorDetails();
		vendor.setFirstName(request.getFName().trim());
		vendor.setMiddleName(isBlank(request.getMName()) ? null : request.getMName().trim());
		vendor.setLastName(request.getLName().trim());
		vendor.setPhoneNo(PhoneNumberUtil.normalize(request.getPhoneNo()));
		vendor.setPassword(request.getPassword());
		vendor.setEmail(isBlank(request.getEmail()) ? null : request.getEmail().trim());
		vendor.setAdharNo(request.getAadharNo().trim());
		vendor.setPanNo(request.getPanNo().trim());
		vendor.setJoiningDate(LocalDateTime.now());
		vendor.setActive(true);

		VendorDetails saved = repo.save(vendor);
		return new VendorRegisterResponseDTO(saved.getVendorId(), "Vendor registered successfully");
	}

	public VendorLoginResponseDTO login(VendorLoginDTO loginDTO) {
		String phoneNo = PhoneNumberUtil.normalize(loginDTO.getPhoneNo());
		VendorDetails vendor = repo.findByPhoneNoAndPassword(phoneNo, loginDTO.getPassword());
		if (vendor == null || !vendor.isActive()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
		}

		return new VendorLoginResponseDTO(
				"Login successful",
				VendorProfileDTO.from(vendor),
				vendorShopService.listShops(vendor.getVendorId()));
	}

	private void validateRegistration(VendorRegisterRequestDTO request) {
		if (isBlank(request.getFName()) || isBlank(request.getLName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First and last name are required");
		}
		if (isBlank(request.getPhoneNo())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number is required");
		}
		if (isBlank(request.getPassword())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
		}
		if (isBlank(request.getAadharNo())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aadhar number is required");
		}
		if (isBlank(request.getPanNo())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PAN number is required");
		}
	}

	private boolean isBlank(String value) {
		return value == null || value.isBlank();
	}
}
