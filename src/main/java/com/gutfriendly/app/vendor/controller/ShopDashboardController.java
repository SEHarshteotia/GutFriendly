package com.gutfriendly.app.vendor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.vendor.dto.OrderOverviewResponseDTO;
import com.gutfriendly.app.vendor.dto.ShopActiveOrderListResponseDTO;
import com.gutfriendly.app.vendor.dto.ShopDashboardResponseDTO;
import com.gutfriendly.app.vendor.dto.ShopDashboardSummaryDTO;
import com.gutfriendly.app.vendor.dto.ShopRecentReviewListResponseDTO;
import com.gutfriendly.app.vendor.dto.ShopStatusDTO;
import com.gutfriendly.app.vendor.dto.ShopTopSellingItemListResponseDTO;
import com.gutfriendly.app.vendor.service.ShopDashboardService;

/**
 * REST API for vendor shop dashboard and analytics.
 * <p>
 * Base path: {@code /vendor}
 */
@RestController
@RequestMapping("/vendor")
@CrossOrigin(origins = "http://localhost:5173")
public class ShopDashboardController {

	private final ShopDashboardService service;

	ShopDashboardController(ShopDashboardService service) {
		this.service = service;
	}

	/**
	 * Returns the full dashboard payload for a shop, including summary, active orders,
	 * top items, reviews, and onboarding status.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/dashboard}
	 * Response: {@link ShopDashboardResponseDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/dashboard")
	public ResponseEntity<ShopDashboardResponseDTO> getDashboard(@PathVariable Integer vendorId,
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
	 * Response: {@link ShopDashboardSummaryDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/dashboard/summary")
	public ResponseEntity<ShopDashboardSummaryDTO> getDashboardSummary(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(service.getDashboardSummary(vendorId, shopId));
	}

	/**
	 * Returns the most recent active orders for dashboard display.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/dashboard/active-orders}
	 * Response: {@link ShopActiveOrderListResponseDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/dashboard/active-orders")
	public ResponseEntity<ShopActiveOrderListResponseDTO> getActiveOrders(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(new ShopActiveOrderListResponseDTO(service.getActiveOrders(vendorId, shopId)));
	}

	/**
	 * Returns today's top-selling menu items for the shop.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/dashboard/top-selling-items}
	 * Response: {@link ShopTopSellingItemListResponseDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/dashboard/top-selling-items")
	public ResponseEntity<ShopTopSellingItemListResponseDTO> getTopSellingItems(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(new ShopTopSellingItemListResponseDTO(service.getTopSellingItems(vendorId, shopId)));
	}

	/**
	 * Returns the most recent customer reviews for dashboard display.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/dashboard/recent-reviews}
	 * Response: {@link ShopRecentReviewListResponseDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/dashboard/recent-reviews")
	public ResponseEntity<ShopRecentReviewListResponseDTO> getRecentReviews(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(new ShopRecentReviewListResponseDTO(service.getRecentReviews(vendorId, shopId)));
	}

	/**
	 * Returns operational shop status flags (open, online orders, delivery partners).
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/dashboard/shop-status}
	 * Response: {@link ShopStatusDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/dashboard/shop-status")
	public ResponseEntity<ShopStatusDTO> getShopStatus(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(service.getShopStatus(vendorId, shopId));
	}

	/**
	 * Re-evaluates shop serviceability based on the saved address pincode and refreshes the dashboard.
	 * <p>
	 * Path: {@code POST /vendor/{vendorId}/shops/{shopId}/serviceability/recheck}
	 * Response: {@link ShopDashboardResponseDTO}
	 */
	@PostMapping("/{vendorId}/shops/{shopId}/serviceability/recheck")
	public ResponseEntity<ShopDashboardResponseDTO> recheckServiceability(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(service.recheckServiceability(vendorId, shopId));
	}
}
