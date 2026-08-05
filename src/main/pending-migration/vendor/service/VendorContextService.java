package com.gutfriendly.app.vendor.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gutfriendly.app.vendor.model.VendorDetails;
import com.gutfriendly.app.vendor.model.Store;
import com.gutfriendly.app.vendor.repository.VendorRepo;
import com.gutfriendly.app.vendor.repository.StoreRepo;

/**
 * Shared helper for resolving vendor and shop entities with ownership validation.
 * <p>
 * Used by menu, order, payout, review, and settings services to load
 * {@link VendorDetails} and {@link Store} and throw {@link ResponseStatusException}
 * when the vendor or shop does not exist or does not belong together.
 */
@Service
public class VendorContextService {

	private final VendorRepo vendorRepository;
	private final StoreRepo storeRepository;

	VendorContextService(VendorRepo vendorRepository, StoreRepo storeRepository) {
		this.vendorRepository = vendorRepository;
		this.storeRepository = storeRepository;
	}

	/**
	 * Loads a vendor by ID.
	 *
	 * @param vendorId the vendor's primary key
	 * @return the vendor entity
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if not found
	 */
	public VendorDetails findVendor(Integer vendorId) {
		return vendorRepository.findById(vendorId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found"));
	}

	/**
	 * Loads a shop that belongs to the given vendor.
	 *
	 * @param vendorId the owning vendor's primary key
	 * @param shopId the shop's primary key
	 * @return the shop entity
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor or shop not found
	 */
	public Store findShop(Integer vendorId, Long shopId) {
		VendorDetails vendor = findVendor(vendorId);
		return storeRepository.findByStoreIdAndVendor(shopId, vendor)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found"));
	}
}
