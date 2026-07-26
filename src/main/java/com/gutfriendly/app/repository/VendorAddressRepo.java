package com.gutfriendly.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.model.VendorAddress;
import com.gutfriendly.app.model.VendorDetails;

public interface VendorAddressRepo extends JpaRepository<VendorAddress, Long> {
	Optional<VendorAddress> findByVendor(VendorDetails vendor);
}
