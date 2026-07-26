package com.gutfriendly.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gutfriendly.app.model.VendorDetails;
import com.gutfriendly.app.model.VendorReview;

public interface VendorReviewRepo extends JpaRepository<VendorReview, Long> {

	long countByVendor(VendorDetails vendor);

	List<VendorReview> findTop3ByVendorOrderByCreatedAtDesc(VendorDetails vendor);

	@Query("select coalesce(avg(r.rating), 0) from VendorReview r where r.vendor = :vendor")
	Double averageRatingByVendor(@Param("vendor") VendorDetails vendor);
}
