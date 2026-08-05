package com.gutfriendly.app.vendor.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.vendor.model.ServiceableArea;

/**
 * Persistence access for {@link ServiceableArea} pincode records.
 */
public interface ServiceableAreaRepo extends JpaRepository<ServiceableArea, Integer> {

	/** Returns whether the given pincode is in the serviceable area list. */
	boolean existsByPincode(String pincode);
}
