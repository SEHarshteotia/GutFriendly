package com.gutfriendly.app.vendor.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.gutfriendly.app.admin.model.VendorDetails;
import com.gutfriendly.app.admin.repository.VendorDetailsRepository;
import com.gutfriendly.app.common.security.PasswordHasher;
import com.gutfriendly.app.vendor.dto.ChangePasswordRequestDTO;
import com.gutfriendly.app.vendor.dto.ChangePhoneRequestDTO;
import com.gutfriendly.app.vendor.dto.UpdateVendorProfileRequestDTO;
import com.gutfriendly.app.vendor.dto.VendorProfileDTO;
import com.gutfriendly.app.common.validation.RegistrationValidator;
import com.gutfriendly.app.vendor.util.PhoneNumberUtil;

@Service
public class VendorSettingsService {

	private final VendorContextService contextService;
	private final VendorDetailsRepository vendorRepository;

	VendorSettingsService(VendorContextService contextService, VendorDetailsRepository vendorRepository) {
		this.contextService = contextService;
		this.vendorRepository = vendorRepository;
	}

	@Transactional(readOnly = true)
	public VendorProfileDTO getProfile(Integer vendorId) {
		return VendorProfileDTO.from(contextService.findVendor(vendorId));
	}

	@Transactional
	public VendorProfileDTO updateProfile(Integer vendorId, UpdateVendorProfileRequestDTO request) {
		VendorDetails vendor = contextService.findVendor(vendorId);

		if (request.getFName() != null && !request.getFName().isBlank()) {
			vendor.setFirstName(request.getFName().trim());
		}
		if (request.getMName() != null) {
			vendor.setMiddleName(request.getMName().isBlank() ? null : request.getMName().trim());
		}
		if (request.getLName() != null && !request.getLName().isBlank()) {
			vendor.setLastName(request.getLName().trim());
		}
		if (request.getEmail() != null) {
			vendor.setEmail(request.getEmail().isBlank() ? null : request.getEmail().trim());
		}
		if (request.getAadharNo() != null) {
			vendor.setAdharNo(request.getAadharNo().isBlank() ? null : request.getAadharNo().trim());
		}
		if (request.getPanNo() != null) {
			vendor.setPanNo(request.getPanNo().isBlank() ? null : request.getPanNo().trim());
		}

		return VendorProfileDTO.from(vendorRepository.save(vendor));
	}

	@Transactional
	public void changePassword(Integer vendorId, ChangePasswordRequestDTO request) {
		if (request.getCurrentPassword() == null || request.getNewPassword() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current and new password are required");
		}
		// Was a bare 6-character check, which let a password through here that
		// the signup form would have rejected.
		try {
			RegistrationValidator.validatePassword(request.getNewPassword());
		} catch (RegistrationValidator.ValidationException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}

		VendorDetails vendor = contextService.findVendor(vendorId);
		if (!PasswordHasher.matches(request.getCurrentPassword(), vendor.getPassword())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current password is incorrect");
		}

		vendor.setPassword(PasswordHasher.hash(request.getNewPassword()));
		vendorRepository.save(vendor);
	}

	@Transactional
	public VendorProfileDTO changePhone(Integer vendorId, ChangePhoneRequestDTO request) {
		if (request.getNewPhoneNo() == null || request.getNewPhoneNo().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New phone number is required");
		}
		if (request.getPassword() == null || request.getPassword().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required to change phone number");
		}

		VendorDetails vendor = contextService.findVendor(vendorId);
		if (!PasswordHasher.matches(request.getPassword(), vendor.getPassword())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password is incorrect");
		}

		String normalized = PhoneNumberUtil.normalize(request.getNewPhoneNo());
		if (normalized.equals(vendor.getPhoneNo())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New phone number is the same as the current one");
		}

		VendorDetails existing = vendorRepository.findByPhoneNo(normalized);
		if (existing != null && existing.getVendorId() != vendorId) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number is already registered");
		}

		vendor.setPhoneNo(normalized);
		return VendorProfileDTO.from(vendorRepository.save(vendor));
	}
}
