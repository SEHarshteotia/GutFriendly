package com.gutfriendly.app.vendor.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.gutfriendly.app.admin.model.FoodItemsDetails;
import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.admin.repository.FoodItemsDetailsRepository;
import com.gutfriendly.app.vendor.dto.CreateMenuItemRequestDTO;
import com.gutfriendly.app.vendor.dto.MenuItemDTO;
import com.gutfriendly.app.vendor.dto.UpdateMenuItemRequestDTO;
import com.gutfriendly.app.vendor.enums.MenuItemCategory;

@Service
public class ShopMenuService {

	private final VendorContextService contextService;
	private final FoodItemsDetailsRepository menuItemRepository;

	ShopMenuService(VendorContextService contextService, FoodItemsDetailsRepository menuItemRepository) {
		this.contextService = contextService;
		this.menuItemRepository = menuItemRepository;
	}

	@Transactional(readOnly = true)
	public List<MenuItemDTO> listMenuItems(Integer vendorId, Long shopId, boolean activeOnly) {
		ShopDetails shop = contextService.findShop(vendorId, shopId);
		List<FoodItemsDetails> items = activeOnly
				? menuItemRepository.findByShopAndAvailableTrueOrderByFoodCategoryAscFoodNameAsc(shop)
				: menuItemRepository.findByShopOrderByFoodCategoryAscFoodNameAsc(shop);
		return items.stream().map(MenuItemDTO::from).toList();
	}

	@Transactional(readOnly = true)
	public List<String> listCategories(Integer vendorId, Long shopId) {
		contextService.findShop(vendorId, shopId);
		return Arrays.stream(MenuItemCategory.values()).map(MenuItemCategory::name).toList();
	}

	@Transactional(readOnly = true)
	public MenuItemDTO getMenuItem(Integer vendorId, Long shopId, Long itemId) {
		return MenuItemDTO.from(findMenuItem(vendorId, shopId, itemId));
	}

	@Transactional
	public MenuItemDTO createMenuItem(Integer vendorId, Long shopId, CreateMenuItemRequestDTO request) {
		validateMenuItemRequest(request.getName(), request.getPrice(), request.getCategory());

		ShopDetails shop = contextService.findShop(vendorId, shopId);
		LocalDateTime now = LocalDateTime.now();

		FoodItemsDetails item = new FoodItemsDetails();
		item.setShop(shop);
		item.setFoodName(request.getName().trim());
		item.setFoodCategory(parseCategory(request.getCategory()).name());
		item.setFoodDesc(request.getDescription() != null ? request.getDescription() : "");
		item.setPrice(request.getPrice());
		item.setAvailable(true);
		item.setCreatedAt(now);
		item.setUpdatedAt(now);

		return MenuItemDTO.from(menuItemRepository.save(item));
	}

	@Transactional
	public MenuItemDTO updateMenuItem(Integer vendorId, Long shopId, Long itemId, UpdateMenuItemRequestDTO request) {
		FoodItemsDetails item = findMenuItem(vendorId, shopId, itemId);

		if (request.getName() != null && !request.getName().isBlank()) {
			item.setFoodName(request.getName().trim());
		}
		if (request.getCategory() != null) {
			item.setFoodCategory(parseCategory(request.getCategory()).name());
		}
		if (request.getDescription() != null) {
			item.setFoodDesc(request.getDescription());
		}
		if (request.getPrice() != null) {
			item.setPrice(request.getPrice());
		}
		if (request.getActive() != null) {
			item.setAvailable(request.getActive());
		}
		item.setUpdatedAt(LocalDateTime.now());

		return MenuItemDTO.from(menuItemRepository.save(item));
	}

	@Transactional
	public MenuItemDTO toggleMenuItem(Integer vendorId, Long shopId, Long itemId) {
		FoodItemsDetails item = findMenuItem(vendorId, shopId, itemId);
		item.setAvailable(!item.isAvailable());
		item.setUpdatedAt(LocalDateTime.now());
		return MenuItemDTO.from(menuItemRepository.save(item));
	}

	@Transactional
	public void deleteMenuItem(Integer vendorId, Long shopId, Long itemId) {
		FoodItemsDetails item = findMenuItem(vendorId, shopId, itemId);
		menuItemRepository.delete(item);
	}

	private FoodItemsDetails findMenuItem(Integer vendorId, Long shopId, Long itemId) {
		ShopDetails shop = contextService.findShop(vendorId, shopId);
		return menuItemRepository.findByFoodIdAndShop(itemId.intValue(), shop)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu item not found"));
	}

	private void validateMenuItemRequest(String name, java.math.BigDecimal price, String category) {
		if (name == null || name.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item name is required");
		}
		if (price == null || price.signum() <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid price is required");
		}
		if (category == null || category.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category is required");
		}
		parseCategory(category);
	}

	private MenuItemCategory parseCategory(String category) {
		try {
			return MenuItemCategory.valueOf(category.trim().toUpperCase());
		} catch (IllegalArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category: " + category);
		}
	}
}
