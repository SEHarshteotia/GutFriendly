package com.gutfriendly.app.reviews.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gutfriendly.app.reviews.model.ShopReview;

public interface ShopReviewRepository extends JpaRepository<ShopReview, Integer> {

	boolean existsByOrderOrderId(int orderId);

	Optional<ShopReview> findByOrderOrderId(int orderId);

	Page<ShopReview> findByShopShopIdAndActiveTrue(int shopId, Pageable pageable);

	@Query("""
			SELECT AVG(r.rating)
			FROM ShopReview r
			WHERE r.shop.shopId = :shopId
			  AND r.active = true
			""")
	Double calculateAverageRating(@Param("shopId") int shopId);

	long countByShopShopIdAndActiveTrue(int shopId);

	long countByShopShopIdAndRatingAndActiveTrue(int shopId, int rating);

	List<ShopReview> findByShop_ShopIdAndActiveTrueOrderByCreatedAtDesc(int shopId);

	List<ShopReview> findTop3ByShop_ShopIdAndActiveTrueOrderByCreatedAtDesc(int shopId);

	List<ShopReview> findByShop_ShopIdAndActiveTrueAndRatingOrderByCreatedAtDesc(int shopId, int rating);
	
	// ---- Admin: platform-wide review moderation & listing ----

		
		
		Page<ShopReview> findByActiveTrue(Pageable pageable);

		Page<ShopReview> findByRatingAndActiveTrue(int rating, Pageable pageable);

		Page<ShopReview> findByShop_ShopNameContainingIgnoreCaseAndActiveTrue(String shopName, Pageable pageable);

		Page<ShopReview> findByShop_ShopNameContainingIgnoreCaseAndRatingAndActiveTrue(String shopName, int rating,
				Pageable pageable);

		Page<ShopReview> findByShop_ShopIdAndActiveTrue(int shopId, Pageable pageable);

		Page<ShopReview> findByShop_ShopIdAndRatingAndActiveTrue(int shopId, int rating, Pageable pageable);

		@Query("""
				SELECT AVG(r.rating)
				FROM ShopReview r
				WHERE r.active = true
				""")
		Double calculatePlatformAverageRating();

		long countByActiveTrue();

		long countByRatingAndActiveTrue(int rating);

		@Query("""
				SELECT COUNT(DISTINCT r.shop.shopId)
				FROM ShopReview r
				WHERE r.active = true
				""")
		long countDistinctShopsWithReviews();
	}


