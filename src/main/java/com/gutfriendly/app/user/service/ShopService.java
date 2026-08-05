package com.gutfriendly.app.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gutfriendly.app.admin.enums.Category;
import com.gutfriendly.app.admin.enums.ShopStatus;
import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.admin.model.ShopImages;
import com.gutfriendly.app.admin.repository.ShopDetailsRepository;
import com.gutfriendly.app.user.dto.ShopCardDTO;
import com.gutfriendly.app.user.dto.ShopDetailsDTO;
import com.gutfriendly.app.user.exception.BadRequestException;
import com.gutfriendly.app.user.exception.ResourceNotFoundException;

@Service
@Transactional(readOnly = true)
public class ShopService {

	private final ShopDetailsRepository shopRepo;

	ShopService(ShopDetailsRepository shopRepo) {
		this.shopRepo = shopRepo;
	}

	public List<ShopCardDTO> getAllShops() {
		return shopRepo.findByStatus(ShopStatus.VERIFIED).stream()
				.filter(this::isVisibleToUsers)
				.map(this::convertToDTO)
				.toList();
	}

	public List<ShopCardDTO> getTrustedVendors() {
		return shopRepo.findByStatusOrderByFinalGutTrustScoreDesc(ShopStatus.VERIFIED).stream()
				.filter(this::isVisibleToUsers)
				.map(this::convertToDTO)
				.toList();
	}

	public List<ShopCardDTO> searchShops(String keyword) {
		if (keyword == null || keyword.trim().isEmpty()) {
			throw new BadRequestException("Search keyword cannot be empty");
		}
		return shopRepo.findByStatusAndShopNameContainingIgnoreCase(ShopStatus.VERIFIED, keyword.trim()).stream()
				.filter(this::isVisibleToUsers)
				.map(this::convertToDTO)
				.toList();
	}

	public List<ShopCardDTO> getShopsByCategory(Category category) {
		if (category == null) {
			throw new BadRequestException("Shop category is required");
		}
		return shopRepo.findByStatusAndCategory(ShopStatus.VERIFIED, category).stream()
				.filter(this::isVisibleToUsers)
				.map(this::convertToDTO)
				.toList();
	}

	public ShopDetailsDTO getById(int id) {
		ShopDetails shop = shopRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Shop not found"));
		if (!isVisibleToUsers(shop)) {
			throw new ResourceNotFoundException("Shop not found");
		}
		return convertToDetailsDTO(shop);
	}

	private boolean isVisibleToUsers(ShopDetails shop) {
		return shop.getStatus() == ShopStatus.VERIFIED && !Boolean.TRUE.equals(shop.getBlocked());
	}

	private ShopCardDTO convertToDTO(ShopDetails shop) {
		String locality = null;
		String imageUrl = shop.getImageUrl();
		String category = shop.getCategory() != null ? shop.getCategory().name() : null;

		if (shop.getAddress_id() != null) {
			locality = shop.getAddress_id().getLocality();
		}

		if (imageUrl == null && shop.getImages() != null) {
			for (ShopImages image : shop.getImages()) {
				if (image != null && image.getImageUrl() != null) {
					imageUrl = image.getImageUrl();
					break;
				}
			}
		}

		return new ShopCardDTO(
				shop.getShopId(),
				shop.getShopName(),
				category,
				locality,
				shop.getFinalGutTrustScore(),
				imageUrl);
	}

	private ShopDetailsDTO convertToDetailsDTO(ShopDetails shop) {
		String locality = null;
		String pincode = null;
		String category = shop.getCategory() != null ? shop.getCategory().name() : null;

		if (shop.getAddress_id() != null) {
			locality = shop.getAddress_id().getLocality();
			if (shop.getAddress_id().getPinCode() != null) {
				pincode = shop.getAddress_id().getPinCode().getPin_code();
			}
		}

		List<String> imageUrls = new ArrayList<>();
		if (shop.getImageUrl() != null) {
			imageUrls.add(shop.getImageUrl());
		}
		if (shop.getImages() != null) {
			for (ShopImages image : shop.getImages()) {
				if (image != null && image.getImageUrl() != null) {
					imageUrls.add(image.getImageUrl());
				}
			}
		}

		return new ShopDetailsDTO(
				shop.getShopId(),
				shop.getShopName(),
				category,
				shop.getUserTrustScore(),
				shop.getInspectionTrustScore(),
				shop.getFinalGutTrustScore(),
				locality,
				pincode,
				imageUrls);
	}
}
