package com.gutfriendly.app.vendor.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.gutfriendly.app.admin.enums.Category;
import com.gutfriendly.app.admin.enums.ShopStatus;
import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.admin.model.VendorDetails;
import com.gutfriendly.app.admin.repository.ShopDetailsRepository;
import com.gutfriendly.app.admin.repository.VendorDetailsRepository;
import com.gutfriendly.app.vendor.dto.CreateShopRequestDTO;
import com.gutfriendly.app.vendor.dto.ShopDTO;
import com.gutfriendly.app.vendor.dto.ShopDetailsDTO;
import com.gutfriendly.app.vendor.dto.ShopRatingDTO;
import com.gutfriendly.app.vendor.dto.UpdateShopRatingRequestDTO;
import com.gutfriendly.app.vendor.dto.UpdateShopRequestDTO;

@Service
public class VendorShopService {

	private final VendorDetailsRepository vendorRepository;
	private final ShopDetailsRepository shopRepository;

	VendorShopService(VendorDetailsRepository vendorRepository, ShopDetailsRepository shopRepository) {
		this.vendorRepository = vendorRepository;
		this.shopRepository = shopRepository;
	}

	private VendorDetails findVendor(Integer vendorId) {
		return vendorRepository.findById(vendorId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found"));
	}

	private ShopDetails findShopForVendor(Integer vendorId, Long shopId) {
		VendorDetails vendor = findVendor(vendorId);
		return shopRepository.findByShopIdAndVendor(shopId.intValue(), vendor)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found"));
	}

	@Transactional(readOnly = true)
	public List<ShopDTO> listShops(Integer vendorId) {
		VendorDetails vendor = findVendor(vendorId);
		return shopRepository.findByVendor(vendor).stream()
				.map(ShopDTO::from)
				.toList();
	}

	@Transactional(readOnly = true)
	public ShopDTO getShop(Integer vendorId, Long shopId) {
		return ShopDTO.from(findShopForVendor(vendorId, shopId));
	}

	@Transactional
	public ShopDTO createShop(Integer vendorId, CreateShopRequestDTO request) {
		VendorDetails vendor = findVendor(vendorId);

		if (request.getShopName() == null || request.getShopName().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop name is required");
		}

		ShopDetails shop = new ShopDetails();
		shop.setVendor(vendor);
		shop.setShopName(request.getShopName().trim());
		shop.setGstNo("PENDING-" + System.currentTimeMillis());
		shop.setCategory(Category.RESTAURANT);
		shop.setStatus(ShopStatus.PENDING);
		shop.setImageUrl(request.getImageUrl());

		if (request.getOpenTime() != null) {
			shop.setOpenTime(request.getOpenTime());
		}
		if (request.getEstimatedPrepTimeMinutes() != null) {
			shop.setEstimatedPrepTimeMinutes(request.getEstimatedPrepTimeMinutes());
		}

		return ShopDTO.from(shopRepository.save(shop));
	}

	@Transactional(readOnly = true)
	public ShopDetailsDTO getShopDetails(Integer vendorId, Long shopId) {
		return ShopDetailsDTO.from(findShopForVendor(vendorId, shopId));
	}

	@Transactional(readOnly = true)
	public ShopRatingDTO getShopRating(Integer vendorId, Long shopId) {
		return ShopRatingDTO.from(findShopForVendor(vendorId, shopId));
	}

	@Transactional
	public ShopRatingDTO updateShopRating(Integer vendorId, Long shopId, UpdateShopRatingRequestDTO request) {
		if (request.getRating() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating is required");
		}

		ShopDetails shop = findShopForVendor(vendorId, shopId);
		shop.setRating(request.getRating());
		shop.setRatingCount(request.getRatingCount() != null ? request.getRatingCount() : 0L);
		return ShopRatingDTO.from(shopRepository.save(shop));
	}

	@Transactional
	public ShopDetailsDTO updateShopDetails(Integer vendorId, Long shopId, UpdateShopRequestDTO request) {
		updateShop(vendorId, shopId, request);
		return getShopDetails(vendorId, shopId);
	}

	@Transactional
	public ShopDTO updateShop(Integer vendorId, Long shopId, UpdateShopRequestDTO request) {
		ShopDetails shop = findShopForVendor(vendorId, shopId);

		if (request.getShopName() != null && !request.getShopName().isBlank()) {
			shop.setShopName(request.getShopName().trim());
		}
		if (request.getImageUrl() != null) {
			shop.setImageUrl(request.getImageUrl());
		}
		if (request.getIsOpen() != null) {
			shop.setIsOpen(request.getIsOpen());
		}
		if (request.getOpenTime() != null) {
			shop.setOpenTime(request.getOpenTime());
		}
		if (request.getCloseTime() != null) {
			shop.setCloseTime(request.getCloseTime());
		}
		if (request.getOnlineOrdersEnabled() != null) {
			shop.setOnlineOrdersEnabled(request.getOnlineOrdersEnabled());
		}
		if (request.getEstimatedPrepTimeMinutes() != null) {
			shop.setEstimatedPrepTimeMinutes(request.getEstimatedPrepTimeMinutes());
		}

		return ShopDTO.from(shopRepository.save(shop));
	}
}
