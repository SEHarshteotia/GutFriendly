package com.gutfriendly.app.vendor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.vendor.dto.StorePayoutListResponseDTO;
import com.gutfriendly.app.vendor.dto.StorePayoutSummaryDTO;
import com.gutfriendly.app.vendor.service.StorePayoutService;

/**
 * REST API for vendor payout summary and history.
 * <p>
 * Base path: {@code /vendor}
 */
@RestController
@RequestMapping("/vendor")
@CrossOrigin(origins = "http://localhost:5173")
public class StorePayoutController {

	private final StorePayoutService service;

	StorePayoutController(StorePayoutService service) {
		this.service = service;
	}

	/**
	 * Returns aggregate payout balances and completed payout count for a shop.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/payouts/summary}
	 * Response: {@link StorePayoutSummaryDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/payouts/summary")
	public ResponseEntity<StorePayoutSummaryDTO> getPayoutSummary(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(service.getPayoutSummary(vendorId, shopId));
	}

	/**
	 * Lists all payout records for a shop in reverse chronological order.
	 * <p>
	 * Path: {@code GET /vendor/{vendorId}/shops/{shopId}/payouts}
	 * Response: {@link StorePayoutListResponseDTO}
	 */
	@GetMapping("/{vendorId}/shops/{shopId}/payouts")
	public ResponseEntity<StorePayoutListResponseDTO> listPayouts(@PathVariable Integer vendorId,
			@PathVariable Long shopId) {
		return ResponseEntity.ok(new StorePayoutListResponseDTO(service.listPayouts(vendorId, shopId)));
	}
}
