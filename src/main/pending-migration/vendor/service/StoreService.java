package com.gutfriendly.app.vendor.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.gutfriendly.app.vendor.dto.CreateShopRequestDTO;
import com.gutfriendly.app.vendor.dto.StoreDetailsDTO;
import com.gutfriendly.app.vendor.dto.UpdateShopRequestDTO;
import com.gutfriendly.app.vendor.dto.UpdateShopRatingRequestDTO;
import com.gutfriendly.app.vendor.dto.StoreDTO;
import com.gutfriendly.app.vendor.dto.StoreRatingDTO;
import com.gutfriendly.app.vendor.model.VendorDetails;
import com.gutfriendly.app.vendor.model.Store;
import com.gutfriendly.app.vendor.repository.VendorRepo;
import com.gutfriendly.app.vendor.repository.StoreRepo;
import com.gutfriendly.app.vendor.status.VendorStatus;

/**
 * Manages vendor shops: listing, creation, updates, and store detail views.
 */
@Service
public class StoreService {

	private final VendorRepo vendorRepository;
	private final StoreRepo storeRepository;

	StoreService(VendorRepo vendorRepository, StoreRepo storeRepository) {
		this.vendorRepository = vendorRepository;
		this.storeRepository = storeRepository;
	}

	private VendorDetails findVendor(Integer vendorId) {
		return vendorRepository.findById(vendorId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found"));
	}

	private Store findShopForVendor(Integer vendorId, Long shopId) {
		VendorDetails vendor = findVendor(vendorId);
		return storeRepository.findByStoreIdAndVendor(shopId, vendor)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found"));
	}

	/**
	 * Lists all shops belonging to a vendor.
	 *
	 * @param vendorId the vendor's primary key
	 * @return shop summaries for the vendor
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor not found
	 */
	@Transactional(readOnly = true)
	public List<StoreDTO> listShops(Integer vendorId) {
		VendorDetails vendor = findVendor(vendorId);
		return storeRepository.findByVendor(vendor).stream()
				.map(StoreDTO::from)
				.toList();
	}

	/**
	 * Returns a single shop summary for the vendor.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @return the shop summary
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor or shop not found
	 */
	@Transactional(readOnly = true)
	public StoreDTO getShop(Integer vendorId, Long shopId) {
		return StoreDTO.from(findShopForVendor(vendorId, shopId));
	}

	/**
	 * Creates a new shop for the vendor with {@link VendorStatus#PENDING} status.
	 *
	 * @param vendorId the owning vendor's primary key
	 * @param request shop creation payload; {@code storeName} is required
	 * @return the created shop summary
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor not found
	 * @throws ResponseStatusException with {@link HttpStatus#BAD_REQUEST} if store name is missing
	 */
	@Transactional
	public StoreDTO createShop(Integer vendorId, CreateShopRequestDTO request) {
		VendorDetails vendor = findVendor(vendorId);

		if (request.getStoreName() == null || request.getStoreName().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Store name is required");
		}

		Store store = new Store();
		store.setVendor(vendor);
		store.setStoreName(request.getStoreName().trim());
		store.setImageUrl(request.getImageUrl());
		store.setStatus(VendorStatus.PENDING);

		if (request.getOpenTime() != null) {
			store.setOpenTime(request.getOpenTime());
		}

		if (request.getEstimatedPrepTimeMinutes() != null) {
			store.setEstimatedPrepTimeMinutes(request.getEstimatedPrepTimeMinutes());
		}

		return StoreDTO.from(storeRepository.save(store));
	}

	/**
	 * Returns full store details including hours, flags, and address.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @return detailed store view
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor or shop not found
	 */
	@Transactional(readOnly = true)
	public StoreDetailsDTO getStoreDetails(Integer vendorId, Long shopId) {
		return StoreDetailsDTO.from(findShopForVendor(vendorId, shopId));
	}

	/**
	 * Returns the shop's cached rating.
	 * Rating values are stored on the shop and updated via {@link #updateShopRating}.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @return current rating snapshot
	 */
	@Transactional(readOnly = true)
	public StoreRatingDTO getShopRating(Integer vendorId, Long shopId) {
		return StoreRatingDTO.from(findShopForVendor(vendorId, shopId));
	}

	/**
	 * Updates the shop's cached rating. Called by the rating sync API once implemented.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @param request rating and count to persist
	 * @return updated rating snapshot
	 */
	@Transactional
	public StoreRatingDTO updateShopRating(Integer vendorId, Long shopId, UpdateShopRatingRequestDTO request) {
		if (request.getRating() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating is required");
		}

		Store store = findShopForVendor(vendorId, shopId);
		store.setRating(request.getRating());
		store.setRatingCount(request.getRatingCount() != null ? request.getRatingCount() : 0L);
		return StoreRatingDTO.from(storeRepository.save(store));
	}

	/**
	 * Updates store settings and returns the refreshed store details.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @param request partial update payload
	 * @return updated store details
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor or shop not found
	 */
	@Transactional
	public StoreDetailsDTO updateStoreDetails(Integer vendorId, Long shopId, UpdateShopRequestDTO request) {
		updateShop(vendorId, shopId, request);
		return getStoreDetails(vendorId, shopId);
	}

	/**
	 * Applies partial updates to a shop and returns the shop summary.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @param request partial update payload
	 * @return updated shop summary
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor or shop not found
	 */
	@Transactional
	public StoreDTO updateShop(Integer vendorId, Long shopId, UpdateShopRequestDTO request) {
		Store store = findShopForVendor(vendorId, shopId);

		if (request.getStoreName() != null && !request.getStoreName().isBlank()) {
			store.setStoreName(request.getStoreName().trim());
		}

		if (request.getImageUrl() != null) {
			store.setImageUrl(request.getImageUrl());
		}

		if (request.getIsOpen() != null) {
			store.setIsOpen(request.getIsOpen());
		}

		if (request.getOpenTime() != null) {
			store.setOpenTime(request.getOpenTime());
		}

		if (request.getCloseTime() != null) {
			store.setCloseTime(request.getCloseTime());
		}

		if (request.getOnlineOrdersEnabled() != null) {
			store.setOnlineOrdersEnabled(request.getOnlineOrdersEnabled());
		}

		if (request.getEstimatedPrepTimeMinutes() != null) {
			store.setEstimatedPrepTimeMinutes(request.getEstimatedPrepTimeMinutes());
		}

		return StoreDTO.from(storeRepository.save(store));
	}
}
