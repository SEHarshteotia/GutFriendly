package com.gutfriendly.app.vendor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.vendor.dto.CreateMenuItemRequestDTO;
import com.gutfriendly.app.vendor.dto.MenuCategoriesResponseDTO;
import com.gutfriendly.app.vendor.dto.MenuItemDTO;
import com.gutfriendly.app.vendor.dto.MenuItemListResponseDTO;
import com.gutfriendly.app.vendor.dto.MessageResponseDTO;
import com.gutfriendly.app.vendor.dto.UpdateMenuItemRequestDTO;
import com.gutfriendly.app.vendor.service.StoreMenuService;

/**
 * REST API for shop menu item management.
 * <p>
 * Base path: {@code /vendor}
 */
@RestController
@RequestMapping("/vendor")
@CrossOrigin(origins = "http://localhost:5173")
public class StoreMenuController {

	private final StoreMenuService service;

	StoreMenuController(StoreMenuService service) {
		this.service = service;
	}

	/**
	 * Lists menu items for a shop, optionally filtering to active items only.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/menu}
	 * Response: {@link MenuItemListResponseDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/menu")
	public ResponseEntity<MenuItemListResponseDTO> listMenuItems(@PathVariable Integer vendorId,
			@PathVariable Long shopId,
			@RequestParam(defaultValue = "false") boolean activeOnly) {
		return ResponseEntity.ok(new MenuItemListResponseDTO(service.listMenuItems(vendorId, shopId, activeOnly)));
	}

	/**
	 * Lists valid menu item categories (enum values).
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/menu/categories}
	 * Response: {@link MenuCategoriesResponseDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/menu/categories")
	public ResponseEntity<MenuCategoriesResponseDTO> listCategories(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(new MenuCategoriesResponseDTO(service.listCategories(vendorId, shopId)));
	}

	/**
	 * Returns a single menu item by ID.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/menu/{itemId}}
	 * Response: {@link MenuItemDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/menu/{itemId}")
	public ResponseEntity<MenuItemDTO> getMenuItem(@PathVariable Integer vendorId, @PathVariable Long shopId,
			@PathVariable Long itemId) {
		return ResponseEntity.ok(service.getMenuItem(vendorId, shopId, itemId));
	}

	/**
	 * Creates a new menu item for the shop.
	 * <p>
	 * Path: {@code POST /vendor/{vendorId}/shops/{shopId}/menu}
	 * Request: {@link CreateMenuItemRequestDTO}
	 * Response: {@link MenuItemDTO}
	 */
	@PostMapping("/{vendorId}/shops/{shopId}/menu")
	public ResponseEntity<MenuItemDTO> createMenuItem(@PathVariable Integer vendorId, @PathVariable Long shopId,
			@RequestBody CreateMenuItemRequestDTO request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.createMenuItem(vendorId, shopId, request));
	}

	/**
	 * Updates an existing menu item.
	 * <p>
	 * Path: {@code PUT /vendor/{vendorId}/shops/{shopId}/menu/{itemId}}
	 * Request: {@link UpdateMenuItemRequestDTO}
	 * Response: {@link MenuItemDTO}
	 */
	@PutMapping("/{vendorId}/shops/{shopId}/menu/{itemId}")
	public ResponseEntity<MenuItemDTO> updateMenuItem(@PathVariable Integer vendorId, @PathVariable Long shopId,
			@PathVariable Long itemId, @RequestBody UpdateMenuItemRequestDTO request) {
		return ResponseEntity.ok(service.updateMenuItem(vendorId, shopId, itemId, request));
	}

	/**
	 * Toggles the active/inactive state of a menu item.
	 * <p>
	 * Path: {@code PATCH /vendor/{vendorId}/shops/{shopId}/menu/{itemId}/toggle}
	 * Response: {@link MenuItemDTO}
	 */
	@PatchMapping("/{vendorId}/shops/{shopId}/menu/{itemId}/toggle")
	public ResponseEntity<MenuItemDTO> toggleMenuItem(@PathVariable Integer vendorId, @PathVariable Long shopId,
			@PathVariable Long itemId) {
		return ResponseEntity.ok(service.toggleMenuItem(vendorId, shopId, itemId));
	}

	/**
	 * Permanently deletes a menu item.
	 * <p>
	 * Path: {@code DELETE /vendor/{vendorId}/shops/{shopId}/menu/{itemId}}
	 * Response: {@link MessageResponseDTO}
	 */
	@DeleteMapping("/{vendorId}/shops/{shopId}/menu/{itemId}")
	public ResponseEntity<MessageResponseDTO> deleteMenuItem(@PathVariable Integer vendorId, @PathVariable Long shopId,
			@PathVariable Long itemId) {
		service.deleteMenuItem(vendorId, shopId, itemId);
		return ResponseEntity.ok(new MessageResponseDTO("Menu item deleted successfully"));
	}
}
