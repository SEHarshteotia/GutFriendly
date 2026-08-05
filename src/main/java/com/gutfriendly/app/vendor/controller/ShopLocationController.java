package com.gutfriendly.app.vendor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.vendor.dto.ShopLocationRequestDTO;
import com.gutfriendly.app.vendor.dto.ShopLocationResponseDTO;
import com.gutfriendly.app.vendor.service.ShopLocationService;

/**
 * REST API for saving shop location and checking serviceability.
 * <p>
 * Base path: {@code /vendor}
 */
@RestController
@RequestMapping("/vendor")
@CrossOrigin(origins = "http://localhost:5173")
public class ShopLocationController {

	private final ShopLocationService service;

	ShopLocationController(ShopLocationService service) {
		this.service = service;
	}

	/**
	 * Saves or updates the shop address and evaluates pincode serviceability.
	 * <p>
	 * Path: {@code POST /vendor/{vendorId}/shops/{shopId}/location}
	 * Request: {@link ShopLocationRequestDTO}
	 * Response: {@link ShopLocationResponseDTO}
	 */
	@PostMapping("/{vendorId}/shops/{shopId}/location")
	public ResponseEntity<ShopLocationResponseDTO> saveLocation(@PathVariable Integer vendorId,
			@PathVariable Long shopId, @RequestBody ShopLocationRequestDTO request) {
		request.setVendorId(vendorId);
		request.setShopId(shopId);
		return ResponseEntity.ok(service.saveLocation(request));
	}

}
