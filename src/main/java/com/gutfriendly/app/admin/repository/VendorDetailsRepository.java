package com.gutfriendly.app.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gutfriendly.app.admin.model.VendorDetails;

public interface VendorDetailsRepository extends JpaRepository<VendorDetails, Integer> {

	long countByIsActive(boolean isActive);

	VendorDetails findByPhoneNo(String phoneNo);

	// Used to tell the caller exactly which field collided, instead of
	// relying on the wording of a database constraint error.
	boolean existsByPhoneNo(String phoneNo);

	boolean existsByEmail(String email);

	boolean existsByAdharNo(String adharNo);

	boolean existsByPanNo(String panNo);

	// findByPhoneNoAndPassword was removed: passwords are stored as salted
	// digests now, so they cannot be matched with a WHERE clause. Look the
	// vendor up by phone and verify with PasswordHasher instead.

}
