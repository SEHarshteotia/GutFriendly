package com.gutfriendly.app.vendor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.vendor.model.VendorDetails;

/**
 * Persistence access for {@link VendorDetails} vendor accounts.
 */
public interface VendorRepo extends JpaRepository<VendorDetails, Integer> {

	/** Finds a vendor by phone number and password for login authentication. */
	VendorDetails findByPhoneNoAndPassword(String phoneNo, String password);

	/** Finds a vendor by phone number. */
	VendorDetails findByPhoneNo(String phoneNo);

}
