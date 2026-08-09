
	
	package com.gutfriendly.app.admin.service;

	import org.springframework.data.domain.Page;
	import org.springframework.data.domain.PageRequest;
	import org.springframework.data.domain.Pageable;
	import org.springframework.data.domain.Sort;
	import org.springframework.stereotype.Service;
	import org.springframework.transaction.annotation.Transactional;

	import com.gutfriendly.app.admin.dto.response.AdminReviewSummaryDTO;
	import com.gutfriendly.app.admin.model.ShopDetails;
	import com.gutfriendly.app.admin.repository.ShopDetailsRepository;
	import com.gutfriendly.app.reviews.dto.ReviewDTO;
	import com.gutfriendly.app.reviews.model.ShopReview;
	import com.gutfriendly.app.reviews.repository.ShopReviewRepository;
	import com.gutfriendly.app.user.exception.ResourceNotFoundException;
	import com.gutfriendly.app.user.service.GutTrustScoreService;

	@Service
	public class AdminReviewServiceImpl implements AdminReviewService {

		private final ShopReviewRepository reviewRepo;
		private final ShopDetailsRepository shopRepo;
		private final GutTrustScoreService gutTrustScoreService;

		AdminReviewServiceImpl(ShopReviewRepository reviewRepo, ShopDetailsRepository shopRepo,
				GutTrustScoreService gutTrustScoreService) {
			this.reviewRepo = reviewRepo;
			this.shopRepo = shopRepo;
			this.gutTrustScoreService = gutTrustScoreService;
		}

		@Override
		@Transactional(readOnly = true)
		public Page<ReviewDTO> getAllReviews(int page, int size, String sortBy, String direction, Integer shopId,
				Integer rating, String shopName, boolean includeInactive) {

			Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
			Pageable pageable = PageRequest.of(page, size, sort);

			Page<ShopReview> result;

			if (shopId != null && rating != null) {
				result = reviewRepo.findByShop_ShopIdAndRatingAndActiveTrue(shopId, rating, pageable);
			} else if (shopId != null) {
				result = reviewRepo.findByShop_ShopIdAndActiveTrue(shopId, pageable);
			} else if (shopName != null && !shopName.isBlank() && rating != null) {
				result = reviewRepo.findByShop_ShopNameContainingIgnoreCaseAndRatingAndActiveTrue(shopName.trim(),
						rating, pageable);
			} else if (shopName != null && !shopName.isBlank()) {
				result = reviewRepo.findByShop_ShopNameContainingIgnoreCaseAndActiveTrue(shopName.trim(), pageable);
			} else if (rating != null) {
				result = reviewRepo.findByRatingAndActiveTrue(rating, pageable);
			} else if (includeInactive) {
				result = reviewRepo.findAll(pageable);
			} else {
				result = reviewRepo.findByActiveTrue(pageable);
			}

			return result.map(this::toDto);
		}

		@Override
		@Transactional(readOnly = true)
		public ReviewDTO getReviewById(int reviewId) {
			ShopReview review = reviewRepo.findById(reviewId)
					.orElseThrow(() -> new ResourceNotFoundException("Review not found"));
			return toDto(review);
		}

		@Override
		@Transactional(readOnly = true)
		public AdminReviewSummaryDTO getReviewsSummary() {
			Double averageRating = reviewRepo.calculatePlatformAverageRating();
			if (averageRating == null) {
				averageRating = 0.0;
			}
			averageRating = Math.round(averageRating * 10.0) / 10.0;

			long totalReviews = reviewRepo.countByActiveTrue();
			long shopsReviewed = reviewRepo.countDistinctShopsWithReviews();
			long fiveStar = reviewRepo.countByRatingAndActiveTrue(5);
			long fourStar = reviewRepo.countByRatingAndActiveTrue(4);
			long threeStar = reviewRepo.countByRatingAndActiveTrue(3);
			long twoStar = reviewRepo.countByRatingAndActiveTrue(2);
			long oneStar = reviewRepo.countByRatingAndActiveTrue(1);

			return new AdminReviewSummaryDTO(averageRating, totalReviews, shopsReviewed, fiveStar, fourStar, threeStar,
					twoStar, oneStar);
		}

		@Override
		@Transactional
		public ReviewDTO moderateReview(int reviewId, boolean active, String reason) {
			ShopReview review = reviewRepo.findById(reviewId)
					.orElseThrow(() -> new ResourceNotFoundException("Review not found"));

			review.setActive(active);
			ShopReview saved = reviewRepo.saveAndFlush(review);

			int shopId = saved.getShop().getShopId();
			updateShopUserTrustScore(shopId);
			gutTrustScoreService.recalculateFinalScore(shopId);

			return toDto(saved);
		}

		private void updateShopUserTrustScore(int shopId) {
			ShopDetails shop = shopRepo.findById(shopId)
					.orElseThrow(() -> new ResourceNotFoundException("Shop not found"));

			Double averageRating = reviewRepo.calculateAverageRating(shopId);
			if (averageRating == null) {
				averageRating = 0.0;
			}
			averageRating = Math.round(averageRating * 100.0) / 100.0;

			shop.setUserTrustScore(averageRating);
			shopRepo.save(shop);
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
	}


