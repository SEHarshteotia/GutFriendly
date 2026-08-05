package com.gutfriendly.app.vendor.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gutfriendly.app.vendor.model.StoreOrderItem;
import com.gutfriendly.app.vendor.model.Store;

/**
 * Persistence access for {@link StoreOrderItem} line items.
 */
public interface StoreOrderItemRepo extends JpaRepository<StoreOrderItem, Long> {

	/** Aggregates quantity sold per item name for non-cancelled orders in a date range, ordered by quantity descending. */
	@Query("select i.itemName, sum(i.quantity) from StoreOrderItem i "
			+ "where i.order.store = :store and i.order.createdAt between :start and :end "
			+ "and i.order.status <> com.gutfriendly.app.vendor.status.StoreOrderStatus.CANCELLED "
			+ "group by i.itemName order by sum(i.quantity) desc")
	List<Object[]> findTopSellingItems(@Param("store") Store store,
			@Param("start") LocalDateTime start,
			@Param("end") LocalDateTime end);
}
