package com.gutfriendly.app.admin.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.admin.dto.request.ModerateReviewRequest;
import com.gutfriendly.app.admin.dto.response.AdminReviewSummaryDTO;
import com.gutfriendly.app.admin.service.AdminReviewService;
import com.gutfriendly.app.reviews.dto.ReviewDTO;

@RestController
@RequestMapping("/admin/reviews")
public class AdminReviewController {

	/**
	 * Read-only (plus moderation) view of customer reviews for the admin console.
	 * Reviews are created by users via {@code /reviews/user/{userId}}; this
	 * controller lets admins see every review that lands on a shop, in one place,
	 * and hide/restore reviews that violate policy.
	 * <p>
	 * Base path: {@code /admin/reviews}
	 */

	private final AdminReviewService reviewService;

	AdminReviewController(AdminReviewService reviewService) {
		this.reviewService = reviewService;
	}

	/**
	 * Lists reviews platform-wide, newest first by default. Supports optional
	 * filtering by shop, rating, and shop name search, plus pagination and sorting.
	 * <p>
	 * Path: {@code GET /admin/reviews}
	 */
	@GetMapping
	public ResponseEntity<Page<ReviewDTO>> getAllReviews(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "createdAt") String sortBy,
			@RequestParam(defaultValue = "desc") String direction, @RequestParam(required = false) Integer shopId,
			@RequestParam(required = false) Integer rating, @RequestParam(required = false) String shopName,
			@RequestParam(defaultValue = "false") boolean includeInactive) {

		return ResponseEntity.ok(
				reviewService.getAllReviews(page, size, sortBy, direction, shopId, rating, shopName, includeInactive));
	}

	/**
	 * Returns platform-wide review statistics (average rating, total reviews, star
	 * distribution, shops reviewed).
	 * <p>
	 * Path: {@code GET /admin/reviews/summary}
	 */
	@GetMapping("/summary")
	public ResponseEntity<AdminReviewSummaryDTO> getReviewsSummary() {
		return ResponseEntity.ok(reviewService.getReviewsSummary());
	}

	/**
	 * Fetches a single review by id.
	 * <p>
	 * Path: {@code GET /admin/reviews/{reviewId}}
	 */
	@GetMapping("/{reviewId}")
	public ResponseEntity<ReviewDTO> getReviewById(@PathVariable int reviewId) {
		return ResponseEntity.ok(reviewService.getReviewById(reviewId));
	}

	/**
	 * Hides or restores a review (e.g. for policy violations). Recalculates the
	 * affected shop's trust score.
	 * <p>
	 * Path: {@code PATCH /admin/reviews/{reviewId}/moderate}
	 */
	@PatchMapping("/{reviewId}/moderate")
	public ResponseEntity<ReviewDTO> moderateReview(@PathVariable int reviewId,
			@RequestBody ModerateReviewRequest request) {
		return ResponseEntity.ok(reviewService.moderateReview(reviewId, request.isActive(), request.getReason()));
	}
}
