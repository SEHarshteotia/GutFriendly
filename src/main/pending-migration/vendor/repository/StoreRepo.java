package com.gutfriendly.app.vendor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.vendor.model.VendorDetails;
import com.gutfriendly.app.vendor.model.Store;

/**
 * Persistence access for {@link Store} shop entities.
 */
public interface StoreRepo extends JpaRepository<Store, Long> {

	List<Store> findByVendor(VendorDetails vendor);

	Optional<Store> findByStoreIdAndVendor(Long storeId, VendorDetails vendor);
}
