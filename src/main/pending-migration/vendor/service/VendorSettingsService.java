package com.gutfriendly.app.vendor.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.gutfriendly.app.vendor.dto.ChangePasswordRequestDTO;
import com.gutfriendly.app.vendor.dto.ChangePhoneRequestDTO;
import com.gutfriendly.app.vendor.dto.UpdateVendorProfileRequestDTO;
import com.gutfriendly.app.vendor.dto.VendorProfileDTO;
import com.gutfriendly.app.vendor.model.VendorDetails;
import com.gutfriendly.app.vendor.repository.VendorRepo;
import com.gutfriendly.app.vendor.util.PhoneNumberUtil;

/**
 * Manages vendor account profile updates and password changes.
 */
@Service
public class VendorSettingsService {

	private final VendorContextService contextService;
	private final VendorRepo vendorRepository;

	VendorSettingsService(VendorContextService contextService, VendorRepo vendorRepository) {
		this.contextService = contextService;
		this.vendorRepository = vendorRepository;
	}

	/**
	 * Returns the vendor's profile details.
	 *
	 * @param vendorId the vendor's primary key
	 * @return vendor profile DTO
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor not found
	 */
	@Transactional(readOnly = true)
	public VendorProfileDTO getProfile(Integer vendorId) {
		return VendorProfileDTO.from(contextService.findVendor(vendorId));
	}

	/**
	 * Applies partial updates to the vendor profile.
	 *
	 * @param vendorId the vendor's primary key
	 * @param request partial profile update payload
	 * @return updated vendor profile DTO
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor not found
	 */
	@Transactional
	public VendorProfileDTO updateProfile(Integer vendorId, UpdateVendorProfileRequestDTO request) {
		VendorDetails vendor = contextService.findVendor(vendorId);

		if (request.getFName() != null && !request.getFName().isBlank()) {
			vendor.setFName(request.getFName().trim());
		}
		if (request.getMName() != null) {
			vendor.setMName(request.getMName().isBlank() ? null : request.getMName().trim());
		}
		if (request.getLName() != null && !request.getLName().isBlank()) {
			vendor.setLName(request.getLName().trim());
		}
		if (request.getEmail() != null) {
			vendor.setEmail(request.getEmail().isBlank() ? null : request.getEmail().trim());
		}
		if (request.getAadharNo() != null) {
			vendor.setAadharNo(request.getAadharNo().isBlank() ? null : request.getAadharNo().trim());
		}
		if (request.getPanNo() != null) {
			vendor.setPanNo(request.getPanNo().isBlank() ? null : request.getPanNo().trim());
		}

		return VendorProfileDTO.from(vendorRepository.save(vendor));
	}

	/**
	 * Changes the vendor password after validating the current password.
	 *
	 * @param vendorId the vendor's primary key
	 * @param request current and new password; new password must be at least 6 characters
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor not found
	 * @throws ResponseStatusException with {@link HttpStatus#BAD_REQUEST} if passwords missing or new password too short
	 * @throws ResponseStatusException with {@link HttpStatus#UNAUTHORIZED} if current password is incorrect
	 */
	@Transactional
	public void changePassword(Integer vendorId, ChangePasswordRequestDTO request) {
		if (request.getCurrentPassword() == null || request.getNewPassword() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current and new password are required");
		}
		if (request.getNewPassword().length() < 6) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password must be at least 6 characters");
		}

		VendorDetails vendor = contextService.findVendor(vendorId);
		if (!vendor.getPassword().equals(request.getCurrentPassword())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current password is incorrect");
		}

		vendor.setPassword(request.getNewPassword());
		vendorRepository.save(vendor);
	}

	/**
	 * Changes the vendor phone number after validating the account password.
	 *
	 * @param vendorId the vendor's primary key
	 * @param request new phone number and current password
	 * @return updated vendor profile
	 * @throws ResponseStatusException with {@link HttpStatus#BAD_REQUEST} if fields are missing or phone invalid
	 * @throws ResponseStatusException with {@link HttpStatus#UNAUTHORIZED} if password is incorrect
	 * @throws ResponseStatusException with {@link HttpStatus#CONFLICT} if phone is already registered
	 */
	@Transactional
	public VendorProfileDTO changePhone(Integer vendorId, ChangePhoneRequestDTO request) {
		if (request.getNewPhoneNo() == null || request.getNewPhoneNo().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New phone number is required");
		}
		if (request.getPassword() == null || request.getPassword().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required to change phone number");
		}

		VendorDetails vendor = contextService.findVendor(vendorId);
		if (!vendor.getPassword().equals(request.getPassword())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password is incorrect");
		}

		String normalized = PhoneNumberUtil.normalize(request.getNewPhoneNo());
		if (normalized.equals(vendor.getPhoneNo())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New phone number is the same as the current one");
		}

		VendorDetails existing = vendorRepository.findByPhoneNo(normalized);
		if (existing != null && !existing.getVendor_id().equals(vendorId)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number is already registered");
		}

		vendor.setPhoneNo(normalized);
		return VendorProfileDTO.from(vendorRepository.save(vendor));
	}
}
