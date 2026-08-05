package com.gutfriendly.app.vendor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.vendor.dto.OrderOverviewResponseDTO;
import com.gutfriendly.app.vendor.dto.StoreActiveOrderListResponseDTO;
import com.gutfriendly.app.vendor.dto.StoreDashboardResponseDTO;
import com.gutfriendly.app.vendor.dto.StoreDashboardSummaryDTO;
import com.gutfriendly.app.vendor.dto.StoreRecentReviewListResponseDTO;
import com.gutfriendly.app.vendor.dto.StoreStatusDTO;
import com.gutfriendly.app.vendor.dto.StoreTopSellingItemListResponseDTO;
import com.gutfriendly.app.vendor.service.StoreDashboardService;

/**
 * REST API for vendor shop dashboard and analytics.
 * <p>
 * Base path: {@code /vendor}
 */
@RestController
@RequestMapping("/vendor")
@CrossOrigin(origins = "http://localhost:5173")
public class StoreDashboardController {

	private final StoreDashboardService service;

	StoreDashboardController(StoreDashboardService service) {
		this.service = service;
	}

	/**
	 * Returns the full dashboard payload for a shop, including summary, active orders,
	 * top items, reviews, and onboarding status.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/dashboard}
	 * Response: {@link StoreDashboardResponseDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/dashboard")
	public ResponseEntity<StoreDashboardResponseDTO> getDashboard(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(service.getDashboard(vendorId, shopId));
	}

	/**
	 * Returns hourly order and revenue breakdown for the current day.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/dashboard/order-overview}
	 * Response: {@link OrderOverviewResponseDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/dashboard/order-overview")
	public ResponseEntity<OrderOverviewResponseDTO> getOrderOverview(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(new OrderOverviewResponseDTO(service.getOrderOverview(vendorId, shopId)));
	}

	/**
	 * Returns today's summary metrics with day-over-day change percentages.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/dashboard/summary}
	 * Response: {@link StoreDashboardSummaryDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/dashboard/summary")
	public ResponseEntity<StoreDashboardSummaryDTO> getDashboardSummary(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(service.getDashboardSummary(vendorId, shopId));
	}

	/**
	 * Returns the most recent active orders for dashboard display.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/dashboard/active-orders}
	 * Response: {@link StoreActiveOrderListResponseDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/dashboard/active-orders")
	public ResponseEntity<StoreActiveOrderListResponseDTO> getActiveOrders(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(new StoreActiveOrderListResponseDTO(service.getActiveOrders(vendorId, shopId)));
	}

	/**
	 * Returns today's top-selling menu items for the shop.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/dashboard/top-selling-items}
	 * Response: {@link StoreTopSellingItemListResponseDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/dashboard/top-selling-items")
	public ResponseEntity<StoreTopSellingItemListResponseDTO> getTopSellingItems(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(new StoreTopSellingItemListResponseDTO(service.getTopSellingItems(vendorId, shopId)));
	}

	/**
	 * Returns the most recent customer reviews for dashboard display.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/dashboard/recent-reviews}
	 * Response: {@link StoreRecentReviewListResponseDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/dashboard/recent-reviews")
	public ResponseEntity<StoreRecentReviewListResponseDTO> getRecentReviews(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(new StoreRecentReviewListResponseDTO(service.getRecentReviews(vendorId, shopId)));
	}

	/**
	 * Returns operational store status flags (open, online orders, delivery partners).
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/dashboard/store-status}
	 * Response: {@link StoreStatusDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/dashboard/store-status")
	public ResponseEntity<StoreStatusDTO> getStoreStatus(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(service.getStoreStatus(vendorId, shopId));
	}

	/**
	 * Re-evaluates shop serviceability based on the saved address pincode and refreshes the dashboard.
	 * <p>
	 * Path: {@code POST /vendor/{vendorId}/shops/{shopId}/serviceability/recheck}
	 * Response: {@link StoreDashboardResponseDTO}
	 */
	@PostMapping("/{vendorId}/shops/{shopId}/serviceability/recheck")
	public ResponseEntity<StoreDashboardResponseDTO> recheckServiceability(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(service.recheckServiceability(vendorId, shopId));
	}
}
