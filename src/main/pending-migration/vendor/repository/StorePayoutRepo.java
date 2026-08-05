package com.gutfriendly.app.vendor.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gutfriendly.app.vendor.model.StorePayout;
import com.gutfriendly.app.vendor.model.Store;
import com.gutfriendly.app.vendor.status.PayoutStatus;

/**
 * Persistence access for {@link StorePayout} records and payout aggregates.
 */
public interface StorePayoutRepo extends JpaRepository<StorePayout, Long> {

	List<StorePayout> findByStoreOrderByCreatedAtDesc(Store store);

	/** Sums payout amounts for a store filtered by status. */
	@Query("select coalesce(sum(p.amount), 0) from StorePayout p "
			+ "where p.store = :store and p.status = :status")
	BigDecimal sumAmountByStoreAndStatus(@Param("store") Store store,
			@Param("status") PayoutStatus status);

	long countByStoreAndStatus(Store store, PayoutStatus status);

	Optional<StorePayout> findByPayoutIdAndStore(Long payoutId, Store store);
}
