package com.gutfriendly.app.vendor.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gutfriendly.app.vendor.model.StoreOrder;
import com.gutfriendly.app.vendor.model.Store;
import com.gutfriendly.app.vendor.status.StoreOrderStatus;

/**
 * Persistence access for {@link StoreOrder} entities and order analytics queries.
 */
public interface StoreOrderRepo extends JpaRepository<StoreOrder, Long> {

	long countByStoreAndCreatedAtBetween(Store store, LocalDateTime start, LocalDateTime end);

	long countByStoreAndStatusIn(Store store, Collection<StoreOrderStatus> statuses);

	List<StoreOrder> findTop5ByStoreAndStatusInOrderByCreatedAtDesc(Store store,
			Collection<StoreOrderStatus> statuses);

	List<StoreOrder> findByStoreAndStatusInOrderByCreatedAtDesc(Store store,
			Collection<StoreOrderStatus> statuses);

	List<StoreOrder> findByStoreOrderByCreatedAtDesc(Store store);

	Optional<StoreOrder> findByOrderIdAndStore(Long orderId, Store store);

	/** Sums total amount for non-cancelled orders in a date range. */
	@Query("select coalesce(sum(o.totalAmount), 0) from StoreOrder o "
			+ "where o.store = :store and o.createdAt between :start and :end "
			+ "and o.status <> com.gutfriendly.app.vendor.status.StoreOrderStatus.CANCELLED")
	BigDecimal sumRevenueByStoreAndCreatedAtBetween(@Param("store") Store store,
			@Param("start") LocalDateTime start,
			@Param("end") LocalDateTime end);

	/** Returns per-hour order count and revenue for non-cancelled orders in a date range. */
	@Query("select hour(o.createdAt), count(o), coalesce(sum(o.totalAmount), 0) from StoreOrder o "
			+ "where o.store = :store and o.createdAt between :start and :end "
			+ "and o.status <> com.gutfriendly.app.vendor.status.StoreOrderStatus.CANCELLED "
			+ "group by hour(o.createdAt) order by hour(o.createdAt)")
	List<Object[]> findHourlyOrderStats(@Param("store") Store store,
			@Param("start") LocalDateTime start,
			@Param("end") LocalDateTime end);
}
