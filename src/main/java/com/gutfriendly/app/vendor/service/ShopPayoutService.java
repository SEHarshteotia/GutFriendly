package com.gutfriendly.app.vendor.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.vendor.dto.ShopPayoutDTO;
import com.gutfriendly.app.vendor.dto.ShopPayoutSummaryDTO;
import com.gutfriendly.app.vendor.repository.ShopPayoutRepo;
import com.gutfriendly.app.vendor.status.PayoutStatus;

@Service
public class ShopPayoutService {

	private final VendorContextService contextService;
	private final ShopPayoutRepo payoutRepository;

	ShopPayoutService(VendorContextService contextService, ShopPayoutRepo payoutRepository) {
		this.contextService = contextService;
		this.payoutRepository = payoutRepository;
	}

	@Transactional(readOnly = true)
	public ShopPayoutSummaryDTO getPayoutSummary(Integer vendorId, Long shopId) {
		ShopDetails shop = contextService.findShop(vendorId, shopId);

		var pendingBalance = payoutRepository.sumAmountByShopAndStatus(shop, PayoutStatus.PENDING);
		var totalPaidOut = payoutRepository.sumAmountByShopAndStatus(shop, PayoutStatus.COMPLETED);
		long completedPayouts = payoutRepository.countByShopAndStatus(shop, PayoutStatus.COMPLETED);
		var totalEarned = totalPaidOut.add(pendingBalance);

		return new ShopPayoutSummaryDTO(
				pendingBalance,
				totalEarned,
				totalPaidOut,
				completedPayouts);
	}

	@Transactional(readOnly = true)
	public List<ShopPayoutDTO> listPayouts(Integer vendorId, Long shopId) {
		ShopDetails shop = contextService.findShop(vendorId, shopId);
		return payoutRepository.findByShopOrderByCreatedAtDesc(shop).stream()
				.map(ShopPayoutDTO::from)
				.toList();
	}
}
