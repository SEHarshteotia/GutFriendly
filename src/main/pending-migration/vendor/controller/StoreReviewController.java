package com.gutfriendly.app.vendor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.vendor.dto.StoreReviewDTO;
import com.gutfriendly.app.vendor.dto.StoreReviewListResponseDTO;
import com.gutfriendly.app.vendor.dto.StoreReviewReplyRequestDTO;
import com.gutfriendly.app.vendor.dto.StoreReviewStatsDTO;
import com.gutfriendly.app.vendor.service.StoreReviewService;

/**
 * REST API for customer review listing, statistics, and vendor replies.
 * <p>
 * Base path: {@code /vendor}
 */
@RestController
@RequestMapping("/vendor")
@CrossOrigin(origins = "http://localhost:5173")
public class StoreReviewController {

	private final StoreReviewService service;

	StoreReviewController(StoreReviewService service) {
		this.service = service;
	}

	/**
	 * Lists all reviews for a shop in reverse chronological order.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/reviews}
	 * Response: {@link StoreReviewListResponseDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/reviews")
	public ResponseEntity<StoreReviewListResponseDTO> listReviews(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(new StoreReviewListResponseDTO(service.listReviews(vendorId, shopId)));
	}

	/**
	 * Returns average rating and per-star count breakdown for a shop.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/reviews/stats}
	 * Response: {@link StoreReviewStatsDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/reviews/stats")
	public ResponseEntity<StoreReviewStatsDTO> getReviewStats(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(service.getReviewStats(vendorId, shopId));
	}

	/**
	 * Posts a vendor reply to a customer review.
	 * <p>
	 * Path: {@code POST /vendor/{vendorId}/shops/{shopId}/reviews/{reviewId}/reply}
	 * Request: {@link StoreReviewReplyRequestDTO}
	 * Response: {@link StoreReviewDTO}
	 */
	@PostMapping("/{vendorId}/shops/{shopId}/reviews/{reviewId}/reply")
	public ResponseEntity<StoreReviewDTO> replyToReview(@PathVariable Integer vendorId, @PathVariable Long shopId,
			@PathVariable Long reviewId, @RequestBody StoreReviewReplyRequestDTO request) {
		return ResponseEntity.ok(service.replyToReview(vendorId, shopId, reviewId, request));
	}
}
