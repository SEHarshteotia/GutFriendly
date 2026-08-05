package com.gutfriendly.app.vendor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.vendor.dto.CreateShopRequestDTO;
import com.gutfriendly.app.vendor.dto.UpdateShopRequestDTO;
import com.gutfriendly.app.vendor.dto.VendorLoginDTO;
import com.gutfriendly.app.vendor.dto.VendorLoginResponseDTO;
import com.gutfriendly.app.vendor.dto.VendorRegisterRequestDTO;
import com.gutfriendly.app.vendor.dto.VendorRegisterResponseDTO;
import com.gutfriendly.app.vendor.dto.StoreDTO;
import com.gutfriendly.app.vendor.dto.StoreListResponseDTO;
import com.gutfriendly.app.vendor.service.VendorService;
import com.gutfriendly.app.vendor.service.StoreService;

/**
 * REST API for vendor authentication and shop CRUD.
 * <p>
 * Base path: {@code /vendor}
 */
@RestController
@RequestMapping("/vendor")
@CrossOrigin(origins = "http://localhost:5173")
public class VendorController {

	private final VendorService service;
	private final StoreService shopService;

	VendorController(VendorService service, StoreService shopService) {
		this.service = service;
		this.shopService = shopService;
	}

	/**
	 * Registers a new vendor account.
	 * <p>
	 * Path: {@code POST /vendor/register}
	 * Request: {@link VendorRegisterRequestDTO}
	 * Response: {@link VendorRegisterResponseDTO}
	 */
	@PostMapping("/register")
	public ResponseEntity<VendorRegisterResponseDTO> register(@RequestBody VendorRegisterRequestDTO request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
	}

	/**
	 * Authenticates a vendor by phone number and password.
	 * <p>
	 * Path: {@code POST /vendor/login}
	 * Request: {@link VendorLoginDTO}
	 * Response: {@link VendorLoginResponseDTO}
	 */
	@PostMapping("/login")
	public ResponseEntity<VendorLoginResponseDTO> login(@RequestBody VendorLoginDTO loginDTO) {
		return ResponseEntity.ok(service.login(loginDTO));
	}

	/**
	 * Lists all shops owned by a vendor.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops}
	 * Response: {@link StoreListResponseDTO}
	 */
	@GetMapping("/{vendorId}/shops")
	public ResponseEntity<StoreListResponseDTO> listShops(@PathVariable Integer vendorId) {
		return ResponseEntity.ok(new StoreListResponseDTO(shopService.listShops(vendorId)));
	}

	/**
	 * Returns a single shop by ID for the given vendor.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}}
	 * Response: {@link StoreDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}")
	public ResponseEntity<StoreDTO> getShop(@PathVariable Integer vendorId, @PathVariable Long shopId) {
		return ResponseEntity.ok(shopService.getShop(vendorId, shopId));
	}

	/**
	 * Creates a new shop for the vendor.
	 * <p>
	 * Path: {@code POST /vendor/{vendorId}/shops}
	 * Request: {@link CreateShopRequestDTO}
	 * Response: {@link StoreDTO}
	 */
	@PostMapping("/{vendorId}/shops")
	public ResponseEntity<StoreDTO> createShop(@PathVariable Integer vendorId,
			@RequestBody CreateShopRequestDTO request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(shopService.createShop(vendorId, request));
	}

	/**
	 * Updates an existing shop's details.
	 * <p>
	 * Path: {@code PUT /vendor/{vendorId}/shops/{shopId}}
	 * Request: {@link UpdateShopRequestDTO}
	 * Response: {@link StoreDTO}
	 */
	@PutMapping("/{vendorId}/shops/{shopId}")
	public ResponseEntity<StoreDTO> updateShop(@PathVariable Integer vendorId, @PathVariable Long shopId,
			@RequestBody UpdateShopRequestDTO request) {
		return ResponseEntity.ok(shopService.updateShop(vendorId, shopId, request));
	}
}
