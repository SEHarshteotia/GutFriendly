package com.gutfriendly.app.vendor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.vendor.model.VendorDetails;

public interface VendorRepo extends JpaRepository<VendorDetails, Integer> {

	VendorDetails findByPhoneNoAndPassword(String phoneNo, String password);

}
