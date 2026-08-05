package com.gutfriendly.app.vendor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.admin.dto.response.InspectionResponse;
import com.gutfriendly.app.vendor.dto.BookInspectionRequestDTO;
import com.gutfriendly.app.vendor.service.ShopInspectionService;

/**
 * REST API for vendor inspection booking.
 * <p>
 * Base path: {@code /vendor}
 */
@RestController
@RequestMapping("/vendor")
@CrossOrigin(origins = "http://localhost:5173")
public class ShopInspectionController {

	private final ShopInspectionService service;

	ShopInspectionController(ShopInspectionService service) {
		this.service = service;
	}

	/**
	 * Books an inspection slot for a serviceable shop.
	 * <p>
	 * Path: {@code POST /vendor/{vendorId}/shops/{shopId}/inspections/book}
	 */
	@PostMapping("/{vendorId}/shops/{shopId}/inspections/book")
	public ResponseEntity<InspectionResponse> bookInspection(@PathVariable Integer vendorId,
			@PathVariable Long shopId, @RequestBody BookInspectionRequestDTO request) {
		return ResponseEntity.ok(service.bookInspection(vendorId, shopId, request));
	}
}
