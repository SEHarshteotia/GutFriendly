package com.gutfriendly.app.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gutfriendly.app.model.VendorDetails;
import com.gutfriendly.app.model.VendorOrder;
import com.gutfriendly.app.status.VendorOrderStatus;

public interface VendorOrderRepo extends JpaRepository<VendorOrder, Long> {

	long countByVendorAndCreatedAtBetween(VendorDetails vendor, LocalDateTime start, LocalDateTime end);

	List<VendorOrder> findTop5ByVendorAndStatusInOrderByCreatedAtDesc(VendorDetails vendor,
			Collection<VendorOrderStatus> statuses);

	@Query("select coalesce(sum(o.totalAmount), 0) from VendorOrder o "
			+ "where o.vendor = :vendor and o.createdAt between :start and :end "
			+ "and o.status <> com.gutfriendly.app.status.VendorOrderStatus.CANCELLED")
	BigDecimal sumRevenueByVendorAndCreatedAtBetween(@Param("vendor") VendorDetails vendor,
			@Param("start") LocalDateTime start,
			@Param("end") LocalDateTime end);
}
