package com.gutfriendly.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.model.VendorDetails;
import com.gutfriendly.app.model.VendorStore;

public interface VendorStoreRepo extends JpaRepository<VendorStore, Long> {

	Optional<VendorStore> findByVendor(VendorDetails vendor);
}
