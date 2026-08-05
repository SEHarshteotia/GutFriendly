package com.gutfriendly.app.vendor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.vendor.dto.ShopReviewListResponseDTO;
import com.gutfriendly.app.vendor.dto.ShopReviewStatsDTO;
import com.gutfriendly.app.vendor.service.ShopReviewService;

/**
 * REST API for read-only customer review listing and statistics.
 * <p>
 * Base path: {@code /vendor}
 */
@RestController
@RequestMapping("/vendor")
@CrossOrigin(origins = "http://localhost:5173")
public class ShopReviewController {

	private final ShopReviewService service;

	ShopReviewController(ShopReviewService service) {
		this.service = service;
	}

	/**
	 * Lists active reviews for a shop in reverse chronological order.
	 * Optional {@code rating} query parameter filters by star rating (1-5).
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/reviews}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/reviews")
	public ResponseEntity<ShopReviewListResponseDTO> listReviews(@PathVariable Integer vendorId,
			@PathVariable Long shopId, @RequestParam(required = false) Integer rating) {
		return ResponseEntity.ok(new ShopReviewListResponseDTO(service.listReviews(vendorId, shopId, rating)));
	}

	/**
	 * Returns average rating and per-star count breakdown for a shop.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/reviews/stats}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/reviews/stats")
	public ResponseEntity<ShopReviewStatsDTO> getReviewStats(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(service.getReviewStats(vendorId, shopId));
	}
}
