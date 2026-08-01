package com.gutfriendly.app.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gutfriendly.app.admin.model.VendorDetails;

public interface VendorDetailsRepository extends JpaRepository<VendorDetails, Integer> {
//	@Query("SELECT AVG(v.gutTrustScore) FROM VendorDetails v WHERE v.is_active=true")
//	Double getAverageGutTrustScore();

	long countByIsActive(boolean isActive);
	

}
