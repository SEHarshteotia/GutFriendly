package com.gutfriendly.app.vendor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.vendor.dto.StoreLocationRequestDTO;
import com.gutfriendly.app.vendor.dto.StoreLocationResponseDTO;
import com.gutfriendly.app.vendor.service.StoreLocationService;

/**
 * REST API for saving shop location and checking serviceability.
 * <p>
 * Base path: {@code /vendor}
 */
@RestController
@RequestMapping("/vendor")
@CrossOrigin(origins = "http://localhost:5173")
public class StoreLocationController {

	private final StoreLocationService service;

	StoreLocationController(StoreLocationService service) {
		this.service = service;
	}

	/**
	 * Saves or updates the shop address and evaluates pincode serviceability.
	 * <p>
	 * Path: {@code POST /vendor/{vendorId}/shops/{shopId}/location}
	 * Request: {@link StoreLocationRequestDTO}
	 * Response: {@link StoreLocationResponseDTO}
	 */
	@PostMapping("/{vendorId}/shops/{shopId}/location")
	public ResponseEntity<StoreLocationResponseDTO> saveLocation(@PathVariable Integer vendorId,
			@PathVariable Long shopId, @RequestBody StoreLocationRequestDTO request) {
		request.setVendorId(vendorId);
		request.setShopId(shopId);
		return ResponseEntity.ok(service.saveLocation(request));
	}

}
