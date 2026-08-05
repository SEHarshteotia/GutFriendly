package com.gutfriendly.app.vendor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.vendor.dto.ChangePasswordRequestDTO;
import com.gutfriendly.app.vendor.dto.ChangePhoneRequestDTO;
import com.gutfriendly.app.vendor.dto.MessageResponseDTO;
import com.gutfriendly.app.vendor.dto.UpdateVendorProfileRequestDTO;
import com.gutfriendly.app.vendor.dto.VendorProfileDTO;
import com.gutfriendly.app.vendor.service.VendorSettingsService;

/**
 * REST API for vendor account settings (profile and password).
 * <p>
 * Base path: {@code /vendor}
 */
@RestController
@RequestMapping("/vendor")
@CrossOrigin(origins = "http://localhost:5173")
public class VendorSettingsController {

	private final VendorSettingsService service;

	VendorSettingsController(VendorSettingsService service) {
		this.service = service;
	}

	/**
	 * Returns the vendor's profile details.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/settings/profile}
	 * Response: {@link VendorProfileDTO}
	 */
	@GetMapping("/{vendorId}/settings/profile")
	public ResponseEntity<VendorProfileDTO> getProfile(@PathVariable Integer vendorId) {
		return ResponseEntity.ok(service.getProfile(vendorId));
	}

	/**
	 * Updates the vendor's profile fields.
	 * <p>
	 * Path: {@code PUT /vendor/{vendorId}/settings/profile}
	 * Request: {@link UpdateVendorProfileRequestDTO}
	 * Response: {@link VendorProfileDTO}
	 */
	@PutMapping("/{vendorId}/settings/profile")
	public ResponseEntity<VendorProfileDTO> updateProfile(@PathVariable Integer vendorId,
			@RequestBody UpdateVendorProfileRequestDTO request) {
		return ResponseEntity.ok(service.updateProfile(vendorId, request));
	}

	/**
	 * Changes the vendor's password after validating the current password.
	 * <p>
	 * Path: {@code POST /vendor/{vendorId}/settings/change-password}
	 * Request: {@link ChangePasswordRequestDTO}
	 * Response: {@link MessageResponseDTO}
	 */
	@PostMapping("/{vendorId}/settings/change-password")
	public ResponseEntity<MessageResponseDTO> changePassword(@PathVariable Integer vendorId,
			@RequestBody ChangePasswordRequestDTO request) {
		service.changePassword(vendorId, request);
		return ResponseEntity.ok(new MessageResponseDTO("Password updated successfully"));
	}

	/**
	 * Changes the vendor's phone number after validating the account password.
	 * <p>
	 * Path: {@code POST /vendor/{vendorId}/settings/change-phone}
	 * Request: {@link ChangePhoneRequestDTO}
	 * Response: {@link VendorProfileDTO}
	 */
	@PostMapping("/{vendorId}/settings/change-phone")
	public ResponseEntity<VendorProfileDTO> changePhone(@PathVariable Integer vendorId,
			@RequestBody ChangePhoneRequestDTO request) {
		return ResponseEntity.ok(service.changePhone(vendorId, request));
	}
}
