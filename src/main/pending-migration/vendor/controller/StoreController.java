package com.gutfriendly.app.vendor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.vendor.dto.StoreDetailsDTO;
import com.gutfriendly.app.vendor.dto.UpdateShopRequestDTO;
import com.gutfriendly.app.vendor.dto.UpdateShopRatingRequestDTO;
import com.gutfriendly.app.vendor.dto.StoreRatingDTO;
import com.gutfriendly.app.vendor.service.StoreService;

/**
 * REST API for detailed store settings and operational configuration.
 * <p>
 * Base path: {@code /vendor}
 */
@RestController
@RequestMapping("/vendor")
@CrossOrigin(origins = "http://localhost:5173")
public class StoreController {

	private final StoreService shopService;

	StoreController(StoreService shopService) {
		this.shopService = shopService;
	}

	/**
	 * Returns full store details including hours, flags, and address.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/store}
	 * Response: {@link StoreDetailsDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/store")
	public ResponseEntity<StoreDetailsDTO> getStoreDetails(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(shopService.getStoreDetails(vendorId, shopId));
	}

	/**
	 * Updates store operational settings and returns the refreshed details.
	 * <p>
	 * Path: {@code PUT /vendor/{vendorId}/shops/{shopId}/store}
	 * Request: {@link UpdateShopRequestDTO}
	 * Response: {@link StoreDetailsDTO}
	 */
	@PutMapping("/{vendorId}/shops/{shopId}/store")
	public ResponseEntity<StoreDetailsDTO> updateStore(@PathVariable Integer vendorId, @PathVariable Long shopId,
			@RequestBody UpdateShopRequestDTO request) {
		return ResponseEntity.ok(shopService.updateStoreDetails(vendorId, shopId, request));
	}

	/**
	 * Returns the shop's cached rating.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/rating}
	 * Response: {@link StoreRatingDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/rating")
	public ResponseEntity<StoreRatingDTO> getShopRating(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(shopService.getShopRating(vendorId, shopId));
	}

	/**
	 * Updates the shop's cached rating. Wire your rating sync job to this endpoint.
	 * <p>
	 * Path: {@code PUT /vendor/{vendorId}/shops/{shopId}/rating}
	 * Request: {@link UpdateShopRatingRequestDTO}
	 * Response: {@link StoreRatingDTO}
	 */
	@PutMapping("/{vendorId}/shops/{shopId}/rating")
	public ResponseEntity<StoreRatingDTO> updateShopRating(@PathVariable Integer vendorId,
			@PathVariable Long shopId, @RequestBody UpdateShopRatingRequestDTO request) {
		return ResponseEntity.ok(shopService.updateShopRating(vendorId, shopId, request));
	}
}
