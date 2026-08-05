package com.gutfriendly.app.vendor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.vendor.dto.ShopDetailsDTO;
import com.gutfriendly.app.vendor.dto.UpdateShopRequestDTO;
import com.gutfriendly.app.vendor.dto.UpdateShopRatingRequestDTO;
import com.gutfriendly.app.vendor.dto.ShopRatingDTO;
import com.gutfriendly.app.vendor.service.VendorShopService;

/**
 * REST API for detailed shop settings and operational configuration.
 * <p>
 * Base path: {@code /vendor}
 */
@RestController
@RequestMapping("/vendor")
@CrossOrigin(origins = "http://localhost:5173")
public class VendorShopController {

	private final VendorShopService vendorShopService;

	VendorShopController(VendorShopService vendorShopService) {
		this.vendorShopService = vendorShopService;
	}

	/**
	 * Returns full shop details including hours, flags, and address.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/settings}
	 * Response: {@link ShopDetailsDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/settings")
	public ResponseEntity<ShopDetailsDTO> getShopDetails(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(vendorShopService.getShopDetails(vendorId, shopId));
	}

	/**
	 * Updates shop operational settings and returns the refreshed details.
	 * <p>
	 * Path: {@code PUT /vendor/{vendorId}/shops/{shopId}/settings}
	 * Request: {@link UpdateShopRequestDTO}
	 * Response: {@link ShopDetailsDTO}
	 */
	@PutMapping("/{vendorId}/shops/{shopId}/settings")
	public ResponseEntity<ShopDetailsDTO> updateShop(@PathVariable Integer vendorId, @PathVariable Long shopId,
			@RequestBody UpdateShopRequestDTO request) {
		return ResponseEntity.ok(vendorShopService.updateShopDetails(vendorId, shopId, request));
	}

	/**
	 * Returns the shop's cached rating.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/rating}
	 * Response: {@link ShopRatingDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/rating")
	public ResponseEntity<ShopRatingDTO> getShopRating(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(vendorShopService.getShopRating(vendorId, shopId));
	}

	/**
	 * Updates the shop's cached rating. Wire your rating sync job to this endpoint.
	 * <p>
	 * Path: {@code PUT /vendor/{vendorId}/shops/{shopId}/rating}
	 * Request: {@link UpdateShopRatingRequestDTO}
	 * Response: {@link ShopRatingDTO}
	 */
	@PutMapping("/{vendorId}/shops/{shopId}/rating")
	public ResponseEntity<ShopRatingDTO> updateShopRating(@PathVariable Integer vendorId,
			@PathVariable Long shopId, @RequestBody UpdateShopRatingRequestDTO request) {
		return ResponseEntity.ok(vendorShopService.updateShopRating(vendorId, shopId, request));
	}
}
