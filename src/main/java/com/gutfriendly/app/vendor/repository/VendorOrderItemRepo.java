package com.gutfriendly.app.vendor.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gutfriendly.app.vendor.model.VendorDetails;
import com.gutfriendly.app.vendor.model.VendorOrderItem;

public interface VendorOrderItemRepo extends JpaRepository<VendorOrderItem, Long> {

	@Query("select i.itemName, sum(i.quantity) from VendorOrderItem i "
			+ "where i.order.vendor = :vendor and i.order.createdAt between :start and :end "
			+ "and i.order.status <> com.gutfriendly.app.status.VendorOrderStatus.CANCELLED "
			+ "group by i.itemName order by sum(i.quantity) desc")
	List<Object[]> findTopSellingItems(@Param("vendor") VendorDetails vendor,
			@Param("start") LocalDateTime start,
			@Param("end") LocalDateTime end);
}
