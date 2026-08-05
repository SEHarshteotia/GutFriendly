package com.gutfriendly.app.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gutfriendly.app.admin.enums.ShopStatus;
import com.gutfriendly.app.admin.model.FoodImages;
import com.gutfriendly.app.admin.model.FoodItemsDetails;
import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.admin.repository.FoodItemsDetailsRepository;
import com.gutfriendly.app.admin.repository.ShopDetailsRepository;
import com.gutfriendly.app.user.dto.FoodItemDTO;
import com.gutfriendly.app.user.exception.BadRequestException;
import com.gutfriendly.app.user.exception.ResourceNotFoundException;

@Service
@Transactional(readOnly = true)
public class FoodItemService {

	private final FoodItemsDetailsRepository foodRepo;
	private final ShopDetailsRepository shopRepo;

	FoodItemService(FoodItemsDetailsRepository foodRepo, ShopDetailsRepository shopRepo) {
		this.foodRepo = foodRepo;
		this.shopRepo = shopRepo;
	}

	public Page<FoodItemDTO> getMenuByShop(int shopId, Pageable pageable) {
		ShopDetails shop = shopRepo.findById(shopId)
				.orElseThrow(() -> new ResourceNotFoundException("Shop not found"));
		if (shop.getStatus() != ShopStatus.VERIFIED || Boolean.TRUE.equals(shop.getBlocked())) {
			throw new ResourceNotFoundException("Shop not found");
		}
		return foodRepo.findByShopShopIdAndAvailableTrue(shopId, pageable).map(this::convertToDTO);
	}

	public FoodItemDTO getFoodById(int foodId) {
		FoodItemsDetails food = foodRepo.findById(foodId)
				.orElseThrow(() -> new ResourceNotFoundException("Food item not found"));
		return convertToDTO(food);
	}

	public Page<FoodItemDTO> searchFood(String keyword, Pageable pageable) {
		if (keyword == null || keyword.trim().isEmpty()) {
			throw new BadRequestException("Search keyword cannot be empty");
		}
		return foodRepo.findByFoodNameContainingIgnoreCaseAndAvailableTrue(keyword.trim(), pageable)
				.map(this::convertToDTO);
	}

	private FoodItemDTO convertToDTO(FoodItemsDetails food) {
		List<String> imageUrls = new ArrayList<>();
		if (food.getImages() != null) {
			for (FoodImages image : food.getImages()) {
				if (image != null && image.getImageUrl() != null) {
					imageUrls.add(image.getImageUrl());
				}
			}
		}
		return new FoodItemDTO(
				food.getFoodId(),
				food.getFoodName(),
				food.getPrice(),
				food.getFoodDesc(),
				food.getFoodCategory(),
				food.isAvailable(),
				imageUrls);
	}
}
