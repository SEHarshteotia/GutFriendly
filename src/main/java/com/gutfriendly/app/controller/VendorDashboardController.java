package com.gutfriendly.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.dto.VendorActiveOrderDTO;
import com.gutfriendly.app.dto.VendorDashboardResponseDTO;
import com.gutfriendly.app.dto.VendorDashboardSummaryDTO;
import com.gutfriendly.app.dto.VendorRecentReviewDTO;
import com.gutfriendly.app.dto.VendorStoreStatusDTO;
import com.gutfriendly.app.dto.VendorTopSellingItemDTO;
import com.gutfriendly.app.service.VendorDashboardService;

@RestController
@RequestMapping("/vendor")
@CrossOrigin(origins = "http://localhost:5173")
public class VendorDashboardController {

	private final VendorDashboardService service;

	VendorDashboardController(VendorDashboardService service) {
		this.service = service;
	}

	@GetMapping("/{vendorId}/dashboard")
	public ResponseEntity<VendorDashboardResponseDTO> getDashboard(@PathVariable Integer vendorId) {
		return ResponseEntity.ok(service.getDashboard(vendorId));
	}

	@GetMapping("/{vendorId}/dashboard/summary")
	public ResponseEntity<VendorDashboardSummaryDTO> getDashboardSummary(@PathVariable Integer vendorId) {
		return ResponseEntity.ok(service.getDashboardSummary(vendorId));
	}

	@GetMapping("/{vendorId}/dashboard/active-orders")
	public ResponseEntity<List<VendorActiveOrderDTO>> getActiveOrders(@PathVariable Integer vendorId) {
		return ResponseEntity.ok(service.getActiveOrders(vendorId));
	}

	@GetMapping("/{vendorId}/dashboard/top-selling-items")
	public ResponseEntity<List<VendorTopSellingItemDTO>> getTopSellingItems(@PathVariable Integer vendorId) {
		return ResponseEntity.ok(service.getTopSellingItems(vendorId));
	}

	@GetMapping("/{vendorId}/dashboard/recent-reviews")
	public ResponseEntity<List<VendorRecentReviewDTO>> getRecentReviews(@PathVariable Integer vendorId) {
		return ResponseEntity.ok(service.getRecentReviews(vendorId));
	}

	@GetMapping("/{vendorId}/dashboard/store-status")
	public ResponseEntity<VendorStoreStatusDTO> getStoreStatus(@PathVariable Integer vendorId) {
		return ResponseEntity.ok(service.getStoreStatus(vendorId));
	}

	@PostMapping("/{vendorId}/serviceability/recheck")
	public ResponseEntity<VendorDashboardResponseDTO> recheckServiceability(@PathVariable Integer vendorId) {
		return ResponseEntity.ok(service.recheckServiceability(vendorId));
	}
}
