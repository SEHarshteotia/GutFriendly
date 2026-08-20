package com.gutfriendly.app.vendor.service;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
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
import com.gutfriendly.app.common.security.PasswordHasher;
import com.gutfriendly.app.common.validation.RegistrationValidator;
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
		vendor.setPassword(PasswordHasher.hash(request.getPassword()));
		vendor.setEmail(isBlank(request.getEmail()) ? null : request.getEmail().trim());
		vendor.setAdharNo(isBlank(request.getAadharNo()) ? null : request.getAadharNo().trim());
		vendor.setPanNo(isBlank(request.getPanNo()) ? null : request.getPanNo().trim());
		vendor.setJoiningDate(LocalDateTime.now());
		vendor.setActive(true);

		VendorDetails saved;
		try {
			saved = repo.save(vendor);
		} catch (DataIntegrityViolationException ex) {
			// phone / email / aadhar / pan are all UNIQUE, and a collision on
			// any of them used to surface as an unexplained 500.
			throw new ResponseStatusException(
					HttpStatus.CONFLICT, duplicateMessage(ex));
		}
		return new VendorRegisterResponseDTO(saved.getVendorId(), "Vendor registered successfully");
	}

	public VendorLoginResponseDTO login(VendorLoginDTO loginDTO) {
		String phoneNo = PhoneNumberUtil.normalize(loginDTO.getPhoneNo());

		// Was findByPhoneNoAndPassword, which cannot work once the stored
		// value is a salted digest: look the vendor up, then verify.
		VendorDetails vendor = repo.findByPhoneNo(phoneNo);
		if (vendor == null
				|| !PasswordHasher.matches(loginDTO.getPassword(), vendor.getPassword())
				|| !vendor.isActive()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
		}

		if (PasswordHasher.needsRehash(vendor.getPassword())) {
			vendor.setPassword(PasswordHasher.hash(loginDTO.getPassword()));
			repo.save(vendor);
		}

		return new VendorLoginResponseDTO(
				"Login successful",
				// Aadhaar and PAN are deliberately masked here. The settings
				// screen fetches them separately when it actually needs them.
				VendorProfileDTO.masked(vendor),
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

		// Mirrors the browser checks so direct API calls cannot skip them.
		try {
			RegistrationValidator.validateIndianMobile(request.getPhoneNo());
			RegistrationValidator.validateEmail(request.getEmail(), false);
			RegistrationValidator.validatePassword(request.getPassword());
		} catch (RegistrationValidator.ValidationException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}
	}

	private String duplicateMessage(DataIntegrityViolationException ex) {
		String detail = ex.getMostSpecificCause().getMessage();
		String lower = detail == null ? "" : detail.toLowerCase();

		if (lower.contains("phone")) {
			return "That phone number is already registered";
		}
		if (lower.contains("email")) {
			return "That email address is already registered";
		}
		if (lower.contains("adhar") || lower.contains("aadhar")) {
			return "That Aadhaar number is already registered";
		}
		if (lower.contains("pan")) {
			return "That PAN is already registered";
		}
		return "An account with these details already exists";
	}

	private boolean isBlank(String value) {
		return value == null || value.isBlank();
	}
}
