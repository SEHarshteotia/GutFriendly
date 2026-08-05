package com.gutfriendly.app.vendor.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.reviews.dto.ReviewDTO;
import com.gutfriendly.app.reviews.model.ShopReview;
import com.gutfriendly.app.reviews.repository.ShopReviewRepository;
import com.gutfriendly.app.vendor.dto.ShopReviewStatsDTO;

@Service
public class ShopReviewService {

	private final VendorContextService contextService;
	private final ShopReviewRepository reviewRepository;

	ShopReviewService(VendorContextService contextService, ShopReviewRepository reviewRepository) {
		this.contextService = contextService;
		this.reviewRepository = reviewRepository;
	}

	@Transactional(readOnly = true)
	public List<ReviewDTO> listReviews(Integer vendorId, Long shopId, Integer rating) {
		ShopDetails shop = contextService.findShop(vendorId, shopId);
		List<ShopReview> reviews = rating == null
				? reviewRepository.findByShop_ShopIdAndActiveTrueOrderByCreatedAtDesc(shop.getShopId())
				: reviewRepository.findByShop_ShopIdAndActiveTrueAndRatingOrderByCreatedAtDesc(shop.getShopId(),
						rating);
		return reviews.stream().map(this::toDto).toList();
	}

	@Transactional(readOnly = true)
	public ShopReviewStatsDTO getReviewStats(Integer vendorId, Long shopId) {
		ShopDetails shop = contextService.findShop(vendorId, shopId);
		int shopIdInt = shop.getShopId();

		Double averageRating = reviewRepository.calculateAverageRating(shopIdInt);
		if (averageRating == null) {
			averageRating = 0.0;
		}

		return new ShopReviewStatsDTO(
				roundRating(averageRating),
				reviewRepository.countByShopShopIdAndActiveTrue(shopIdInt),
				reviewRepository.countByShopShopIdAndRatingAndActiveTrue(shopIdInt, 5),
				reviewRepository.countByShopShopIdAndRatingAndActiveTrue(shopIdInt, 4),
				reviewRepository.countByShopShopIdAndRatingAndActiveTrue(shopIdInt, 3),
				reviewRepository.countByShopShopIdAndRatingAndActiveTrue(shopIdInt, 2),
				reviewRepository.countByShopShopIdAndRatingAndActiveTrue(shopIdInt, 1));
	}

	private ReviewDTO toDto(ShopReview review) {
		String userName = review.getUser().getFname() + " " + review.getUser().getLname();
		return new ReviewDTO(
				review.getReviewId(),
				review.getOrder().getOrderId(),
				review.getShop().getShopId(),
				review.getShop().getShopName(),
				userName,
				review.getRating(),
				review.getComment(),
				review.getKeywords(),
				review.getReviewType(),
				review.getPointsAwarded(),
				review.getCreatedAt());
	}

	private double roundRating(Double rating) {
		if (rating == null) {
			return 0;
		}
		return BigDecimal.valueOf(rating).setScale(1, RoundingMode.HALF_UP).doubleValue();
	}
}
