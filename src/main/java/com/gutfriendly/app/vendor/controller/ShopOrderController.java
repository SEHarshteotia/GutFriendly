package com.gutfriendly.app.vendor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.vendor.dto.ActiveOrderCountResponseDTO;
import com.gutfriendly.app.vendor.dto.UpdateOrderStatusRequestDTO;
import com.gutfriendly.app.vendor.dto.ShopOrderDTO;
import com.gutfriendly.app.vendor.dto.ShopOrderListResponseDTO;
import com.gutfriendly.app.vendor.service.ShopOrderService;

/**
 * REST API for vendor order listing and status updates.
 * <p>
 * Base path: {@code /vendor}
 */
@RestController
@RequestMapping("/vendor")
@CrossOrigin(origins = "http://localhost:5173")
public class ShopOrderController {

	private final ShopOrderService service;

	ShopOrderController(ShopOrderService service) {
		this.service = service;
	}

	/**
	 * Returns the count of orders in active statuses (new, preparing, out for delivery).
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/orders/active-count}
	 * Response: {@link ActiveOrderCountResponseDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/orders/active-count")
	public ResponseEntity<ActiveOrderCountResponseDTO> getActiveOrderCount(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(new ActiveOrderCountResponseDTO(service.getActiveOrderCount(vendorId, shopId)));
	}

	/**
	 * Lists orders for a shop, optionally filtered by status or {@code active}.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/orders}
	 * Response: {@link ShopOrderListResponseDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/orders")
	public ResponseEntity<ShopOrderListResponseDTO> listOrders(@PathVariable Integer vendorId,
			@PathVariable Long shopId,
			@RequestParam(required = false) String status) {
		return ResponseEntity.ok(new ShopOrderListResponseDTO(service.listOrders(vendorId, shopId, status)));
	}

	/**
	 * Returns a single order with line items.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/orders/{orderId}}
	 * Response: {@link ShopOrderDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/orders/{orderId}")
	public ResponseEntity<ShopOrderDTO> getOrder(@PathVariable Integer vendorId, @PathVariable Long shopId,
			@PathVariable Long orderId) {
		return ResponseEntity.ok(service.getOrder(vendorId, shopId, orderId));
	}

	/**
	 * Updates the status of an order.
	 * <p>
	 * Path: {@code PATCH /vendor/{vendorId}/shops/{shopId}/orders/{orderId}/status}
	 * Request: {@link UpdateOrderStatusRequestDTO}
	 * Response: {@link ShopOrderDTO}
	 */
	@PatchMapping("/{vendorId}/shops/{shopId}/orders/{orderId}/status")
	public ResponseEntity<ShopOrderDTO> updateOrderStatus(@PathVariable Integer vendorId,
			@PathVariable Long shopId, @PathVariable Long orderId,
			@RequestBody UpdateOrderStatusRequestDTO request) {
		return ResponseEntity.ok(service.updateOrderStatus(vendorId, shopId, orderId, request));
	}
}
