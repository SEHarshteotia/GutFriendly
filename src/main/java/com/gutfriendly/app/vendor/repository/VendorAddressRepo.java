package com.gutfriendly.app.vendor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.vendor.model.VendorAddress;
import com.gutfriendly.app.vendor.model.VendorDetails;

public interface VendorAddressRepo extends JpaRepository<VendorAddress, Long> {
	Optional<VendorAddress> findByVendor(VendorDetails vendor);
}
