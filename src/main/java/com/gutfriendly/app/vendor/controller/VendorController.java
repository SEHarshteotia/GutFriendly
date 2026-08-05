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
import com.gutfriendly.app.vendor.dto.ShopDTO;
import com.gutfriendly.app.vendor.dto.ShopListResponseDTO;
import com.gutfriendly.app.vendor.service.VendorService;
import com.gutfriendly.app.vendor.service.VendorShopService;

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
	private final VendorShopService vendorShopService;

	VendorController(VendorService service, VendorShopService vendorShopService) {
		this.service = service;
		this.vendorShopService = vendorShopService;
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
	 * Response: {@link ShopListResponseDTO}
	 */
	@GetMapping("/{vendorId}/shops")
	public ResponseEntity<ShopListResponseDTO> listShops(@PathVariable Integer vendorId) {
		return ResponseEntity.ok(new ShopListResponseDTO(vendorShopService.listShops(vendorId)));
	}

	/**
	 * Returns a single shop by ID for the given vendor.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}}
	 * Response: {@link ShopDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}")
	public ResponseEntity<ShopDTO> getShop(@PathVariable Integer vendorId, @PathVariable Long shopId) {
		return ResponseEntity.ok(vendorShopService.getShop(vendorId, shopId));
	}

	/**
	 * Creates a new shop for the vendor.
	 * <p>
	 * Path: {@code POST /vendor/{vendorId}/shops}
	 * Request: {@link CreateShopRequestDTO}
	 * Response: {@link ShopDTO}
	 */
	@PostMapping("/{vendorId}/shops")
	public ResponseEntity<ShopDTO> createShop(@PathVariable Integer vendorId,
			@RequestBody CreateShopRequestDTO request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(vendorShopService.createShop(vendorId, request));
	}

	/**
	 * Updates an existing shop's details.
	 * <p>
	 * Path: {@code PUT /vendor/{vendorId}/shops/{shopId}}
	 * Request: {@link UpdateShopRequestDTO}
	 * Response: {@link ShopDTO}
	 */
	@PutMapping("/{vendorId}/shops/{shopId}")
	public ResponseEntity<ShopDTO> updateShop(@PathVariable Integer vendorId, @PathVariable Long shopId,
			@RequestBody UpdateShopRequestDTO request) {
		return ResponseEntity.ok(vendorShopService.updateShop(vendorId, shopId, request));
	}
}
