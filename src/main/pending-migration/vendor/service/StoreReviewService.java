package com.gutfriendly.app.vendor.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.gutfriendly.app.vendor.dto.StoreReviewDTO;
import com.gutfriendly.app.vendor.dto.StoreReviewReplyRequestDTO;
import com.gutfriendly.app.vendor.dto.StoreReviewStatsDTO;
import com.gutfriendly.app.vendor.model.StoreReview;
import com.gutfriendly.app.vendor.model.Store;
import com.gutfriendly.app.vendor.repository.StoreReviewRepo;

/**
 * Lists customer reviews, computes rating statistics, and records vendor replies.
 */
@Service
public class StoreReviewService {

	private final VendorContextService contextService;
	private final StoreReviewRepo reviewRepository;

	StoreReviewService(VendorContextService contextService, StoreReviewRepo reviewRepository) {
		this.contextService = contextService;
		this.reviewRepository = reviewRepository;
	}

	/**
	 * Lists all reviews for a shop in reverse chronological order.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @return review DTOs with elapsed minutes since creation
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor or shop not found
	 */
	@Transactional(readOnly = true)
	public List<StoreReviewDTO> listReviews(Integer vendorId, Long shopId) {
		Store store = contextService.findShop(vendorId, shopId);
		return reviewRepository.findByStoreOrderByCreatedAtDesc(store).stream()
				.map(review -> StoreReviewDTO.from(review, minutesAgo(review.getCreatedAt())))
				.toList();
	}

	/**
	 * Returns average rating and per-star count breakdown for a shop.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @return review statistics DTO
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor or shop not found
	 */
	@Transactional(readOnly = true)
	public StoreReviewStatsDTO getReviewStats(Integer vendorId, Long shopId) {
		Store store = contextService.findShop(vendorId, shopId);
		Double averageRating = reviewRepository.averageRatingByStore(store);
		long totalReviews = reviewRepository.countByStore(store);

		return new StoreReviewStatsDTO(
				roundRating(averageRating),
				totalReviews,
				reviewRepository.countByStoreAndRating(store, 5),
				reviewRepository.countByStoreAndRating(store, 4),
				reviewRepository.countByStoreAndRating(store, 3),
				reviewRepository.countByStoreAndRating(store, 2),
				reviewRepository.countByStoreAndRating(store, 1));
	}

	/**
	 * Saves a vendor reply on a customer review.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @param reviewId the review's primary key
	 * @param request reply payload; {@code reply} text is required
	 * @return updated review DTO including the reply
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor, shop, or review not found
	 * @throws ResponseStatusException with {@link HttpStatus#BAD_REQUEST} if reply text is missing
	 */
	@Transactional
	public StoreReviewDTO replyToReview(Integer vendorId, Long shopId, Long reviewId,
			StoreReviewReplyRequestDTO request) {
		if (request.getReply() == null || request.getReply().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reply text is required");
		}

		Store store = contextService.findShop(vendorId, shopId);
		StoreReview review = reviewRepository.findById(reviewId)
				.filter(r -> r.getStore().getStoreId().equals(store.getStoreId()))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

		review.setVendorReply(request.getReply().trim());
		review.setRepliedAt(LocalDateTime.now());

		StoreReview saved = reviewRepository.save(review);
		return StoreReviewDTO.from(saved, minutesAgo(saved.getCreatedAt()));
	}

	private long minutesAgo(LocalDateTime createdAt) {
		if (createdAt == null) {
			return 0;
		}
		return Math.max(0, Duration.between(createdAt, LocalDateTime.now()).toMinutes());
	}

	private double roundRating(Double rating) {
		if (rating == null) {
			return 0;
		}
		return BigDecimal.valueOf(rating).setScale(1, RoundingMode.HALF_UP).doubleValue();
	}
}
