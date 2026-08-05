package com.gutfriendly.app.vendor.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.gutfriendly.app.vendor.dto.CreateMenuItemRequestDTO;
import com.gutfriendly.app.vendor.enums.MenuItemCategory;
import com.gutfriendly.app.vendor.dto.MenuItemDTO;
import com.gutfriendly.app.vendor.dto.UpdateMenuItemRequestDTO;
import com.gutfriendly.app.vendor.model.MenuItem;
import com.gutfriendly.app.vendor.model.Store;
import com.gutfriendly.app.vendor.repository.MenuItemRepo;

/**
 * CRUD operations for shop menu items including category listing and active toggling.
 */
@Service
public class StoreMenuService {

	private final VendorContextService contextService;
	private final MenuItemRepo menuItemRepository;

	StoreMenuService(VendorContextService contextService, MenuItemRepo menuItemRepository) {
		this.contextService = contextService;
		this.menuItemRepository = menuItemRepository;
	}

	/**
	 * Lists menu items for a shop, optionally restricted to active items.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @param activeOnly when {@code true}, returns only active menu items
	 * @return menu item DTOs ordered by category and name
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor or shop not found
	 */
	@Transactional(readOnly = true)
	public List<MenuItemDTO> listMenuItems(Integer vendorId, Long shopId, boolean activeOnly) {
		Store store = contextService.findShop(vendorId, shopId);
		List<MenuItem> items = activeOnly
				? menuItemRepository.findByStoreAndActiveTrueOrderByCategoryAscNameAsc(store)
				: menuItemRepository.findByStoreOrderByCategoryAscNameAsc(store);
		return items.stream().map(MenuItemDTO::from).toList();
	}

	/**
	 * Returns all valid menu item category enum values.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @return category enum names in declaration order
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor or shop not found
	 */
	@Transactional(readOnly = true)
	public List<String> listCategories(Integer vendorId, Long shopId) {
		contextService.findShop(vendorId, shopId);
		return Arrays.stream(MenuItemCategory.values()).map(MenuItemCategory::name).toList();
	}

	/**
	 * Returns a single menu item by ID.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @param itemId the menu item's primary key
	 * @return the menu item DTO
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor, shop, or item not found
	 */
	@Transactional(readOnly = true)
	public MenuItemDTO getMenuItem(Integer vendorId, Long shopId, Long itemId) {
		return MenuItemDTO.from(findMenuItem(vendorId, shopId, itemId));
	}

	/**
	 * Creates a new active menu item for the shop.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @param request creation payload; {@code name} and positive {@code price} are required
	 * @return the created menu item DTO
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor or shop not found
	 * @throws ResponseStatusException with {@link HttpStatus#BAD_REQUEST} if name or price invalid
	 */
	@Transactional
	public MenuItemDTO createMenuItem(Integer vendorId, Long shopId, CreateMenuItemRequestDTO request) {
		validateMenuItemRequest(request.getName(), request.getPrice(), request.getCategory());

		Store store = contextService.findShop(vendorId, shopId);
		MenuItem item = new MenuItem();
		item.setStore(store);
		item.setName(request.getName().trim());
		item.setCategory(parseCategory(request.getCategory()).name());
		item.setDescription(request.getDescription());
		item.setPrice(request.getPrice());
		item.setImageUrl(request.getImageUrl());
		item.setActive(true);

		return MenuItemDTO.from(menuItemRepository.save(item));
	}

	/**
	 * Applies partial updates to an existing menu item.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @param itemId the menu item's primary key
	 * @param request partial update payload
	 * @return the updated menu item DTO
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor, shop, or item not found
	 */
	@Transactional
	public MenuItemDTO updateMenuItem(Integer vendorId, Long shopId, Long itemId, UpdateMenuItemRequestDTO request) {
		MenuItem item = findMenuItem(vendorId, shopId, itemId);

		if (request.getName() != null && !request.getName().isBlank()) {
			item.setName(request.getName().trim());
		}
		if (request.getCategory() != null) {
			item.setCategory(parseCategory(request.getCategory()).name());
		}
		if (request.getDescription() != null) {
			item.setDescription(request.getDescription());
		}
		if (request.getPrice() != null) {
			item.setPrice(request.getPrice());
		}
		if (request.getImageUrl() != null) {
			item.setImageUrl(request.getImageUrl());
		}
		if (request.getActive() != null) {
			item.setActive(request.getActive());
		}

		return MenuItemDTO.from(menuItemRepository.save(item));
	}

	/**
	 * Flips the active flag on a menu item.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @param itemId the menu item's primary key
	 * @return the updated menu item DTO
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor, shop, or item not found
	 */
	@Transactional
	public MenuItemDTO toggleMenuItem(Integer vendorId, Long shopId, Long itemId) {
		MenuItem item = findMenuItem(vendorId, shopId, itemId);
		item.setActive(!Boolean.TRUE.equals(item.getActive()));
		return MenuItemDTO.from(menuItemRepository.save(item));
	}

	/**
	 * Permanently deletes a menu item.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @param itemId the menu item's primary key
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor, shop, or item not found
	 */
	@Transactional
	public void deleteMenuItem(Integer vendorId, Long shopId, Long itemId) {
		MenuItem item = findMenuItem(vendorId, shopId, itemId);
		menuItemRepository.delete(item);
	}

	private MenuItem findMenuItem(Integer vendorId, Long shopId, Long itemId) {
		Store store = contextService.findShop(vendorId, shopId);
		return menuItemRepository.findByItemIdAndStore(itemId, store)
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
