package com.gutfriendly.app.vendor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.vendor.model.VendorDetails;
import com.gutfriendly.app.vendor.model.VendorStore;

public interface VendorStoreRepo extends JpaRepository<VendorStore, Long> {

	Optional<VendorStore> findByVendor(VendorDetails vendor);
}
