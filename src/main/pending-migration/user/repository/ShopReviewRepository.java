package com.gutfriendly.app.user.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gutfriendly.app.user.model.ShopReview;

public interface ShopReviewRepository
        extends JpaRepository<ShopReview, Integer> {

    boolean existsByOrderOrderId(int orderId);

    Optional<ShopReview> findByOrderOrderId(
            int orderId
    );

    Page<ShopReview> findByShopShopIdAndActiveTrue(
            int shopId,
            Pageable pageable
    );

    @Query("""
            SELECT AVG(r.rating)
            FROM ShopReview r
            WHERE r.shop.shopId = :shopId
              AND r.active = true
            """)
    Double calculateAverageRating(
            @Param("shopId") int shopId
    );

    long countByShopShopIdAndActiveTrue(
            int shopId
    );

    long countByShopShopIdAndRatingAndActiveTrue(
            int shopId,
            int rating
    );
}