package com.gutfriendly.app.vendor.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gutfriendly.app.vendor.dto.StorePayoutDTO;
import com.gutfriendly.app.vendor.dto.StorePayoutSummaryDTO;
import com.gutfriendly.app.vendor.model.Store;
import com.gutfriendly.app.vendor.repository.StorePayoutRepo;
import com.gutfriendly.app.vendor.status.PayoutStatus;

/**
 * Provides payout summary aggregates and payout history for vendor shops.
 */
@Service
public class StorePayoutService {

	private final VendorContextService contextService;
	private final StorePayoutRepo payoutRepository;

	StorePayoutService(VendorContextService contextService, StorePayoutRepo payoutRepository) {
		this.contextService = contextService;
		this.payoutRepository = payoutRepository;
	}

	/**
	 * Computes pending balance, total earned, total paid out, and completed payout count.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @return payout summary DTO
	 * @throws org.springframework.web.server.ResponseStatusException if vendor or shop not found
	 */
	@Transactional(readOnly = true)
	public StorePayoutSummaryDTO getPayoutSummary(Integer vendorId, Long shopId) {
		Store store = contextService.findShop(vendorId, shopId);

		BigDecimal pendingBalance = payoutRepository.sumAmountByStoreAndStatus(store, PayoutStatus.PENDING);
		BigDecimal totalPaidOut = payoutRepository.sumAmountByStoreAndStatus(store, PayoutStatus.COMPLETED);
		long completedPayouts = payoutRepository.countByStoreAndStatus(store, PayoutStatus.COMPLETED);

		BigDecimal totalEarned = totalPaidOut.add(pendingBalance);

		return new StorePayoutSummaryDTO(
				pendingBalance,
				totalEarned,
				totalPaidOut,
				completedPayouts);
	}

	/**
	 * Lists all payout records for a shop in reverse chronological order.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @return payout DTOs
	 * @throws org.springframework.web.server.ResponseStatusException if vendor or shop not found
	 */
	@Transactional(readOnly = true)
	public List<StorePayoutDTO> listPayouts(Integer vendorId, Long shopId) {
		Store store = contextService.findShop(vendorId, shopId);
		return payoutRepository.findByStoreOrderByCreatedAtDesc(store).stream()
				.map(StorePayoutDTO::from)
				.toList();
	}
}
