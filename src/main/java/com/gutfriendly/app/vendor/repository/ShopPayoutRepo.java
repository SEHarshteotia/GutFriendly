package com.gutfriendly.app.vendor.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.vendor.model.ShopPayout;
import com.gutfriendly.app.vendor.enums.PayoutStatus;

public interface ShopPayoutRepo extends JpaRepository<ShopPayout, Long> {

	List<ShopPayout> findByShopOrderByCreatedAtDesc(ShopDetails shop);

	@Query("select coalesce(sum(p.amount), 0) from ShopPayout p "
			+ "where p.shop = :shop and p.status = :status")
	BigDecimal sumAmountByShopAndStatus(@Param("shop") ShopDetails shop,
			@Param("status") PayoutStatus status);

	long countByShopAndStatus(ShopDetails shop, PayoutStatus status);

	Optional<ShopPayout> findByPayoutIdAndShop(Long payoutId, ShopDetails shop);
}
