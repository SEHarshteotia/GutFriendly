package com.gutfriendly.app.vendor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gutfriendly.app.vendor.model.StoreReview;
import com.gutfriendly.app.vendor.model.Store;

/**
 * Persistence access for {@link StoreReview} entities and rating aggregates.
 */
public interface StoreReviewRepo extends JpaRepository<StoreReview, Long> {

	long countByStore(Store store);

	List<StoreReview> findTop3ByStoreOrderByCreatedAtDesc(Store store);

	List<StoreReview> findByStoreOrderByCreatedAtDesc(Store store);

	long countByStoreAndRating(Store store, Integer rating);

	/** Returns the average star rating for a store, or zero if no reviews exist. */
	@Query("select coalesce(avg(r.rating), 0) from StoreReview r where r.store = :store")
	Double averageRatingByStore(@Param("store") Store store);
}
