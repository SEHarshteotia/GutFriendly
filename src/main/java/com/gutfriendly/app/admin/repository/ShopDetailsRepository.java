package com.gutfriendly.app.admin.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gutfriendly.app.admin.enums.ServiceAvailabilityStatus;
import com.gutfriendly.app.admin.enums.ShopStatus;
import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.admin.model.VendorDetails;

public interface ShopDetailsRepository extends JpaRepository<ShopDetails, Integer> {

	List<ShopDetails> findByVendor(VendorDetails vendor);

	Optional<ShopDetails> findByShopIdAndVendor(int shopId, VendorDetails vendor);
	@Query("""
			SELECT
			MONTH(s.lastCalculatedAt),
			AVG(s.finalGutTrustScore)
			FROM ShopDetails s
			GROUP BY MONTH(s.lastCalculatedAt)
			ORDER BY MONTH(s.lastCalculatedAt)
			""")
			List<Object[]> getMonthlyTrustTrend();
			
			@Query("""
				       SELECT
				       s.category,
				       AVG(s.finalGutTrustScore)
				       FROM ShopDetails s
				       GROUP BY s.category
				       ORDER BY AVG(s.finalGutTrustScore) DESC
				       """)
				List<Object[]> getCategoryPerformance();
				
				Page <ShopDetails> findAllByOrderByCreatedAtDesc( Pageable pageable);
                 
				Page<ShopDetails> findAll(Pageable pageable);
				
				Page<ShopDetails> findByStatus(ShopStatus status , Pageable pageable); 
				
				Page<ShopDetails> findByServiceAvailabilityStatus(ServiceAvailabilityStatus serviceAvailabilityStatus,Pageable pageable);
				
				Page<ShopDetails> findByShopNameContainingIgnoreCase(String shopName,Pageable pageable);
				
				Optional<ShopDetails> findByShopId(int shopId);
				
				long countByStatus(ShopStatus status);
				
				long countByBlocked(Boolean blocked);

				List<ShopDetails> findByOrderByFinalGutTrustScoreDesc();

				List<ShopDetails> findByCategory(com.gutfriendly.app.admin.enums.Category category);

				List<ShopDetails> findByShopNameContainingIgnoreCase(String shopName);

				List<ShopDetails> findByStatus(ShopStatus status);

				List<ShopDetails> findByStatusOrderByFinalGutTrustScoreDesc(ShopStatus status);

				List<ShopDetails> findByStatusAndCategory(ShopStatus status,
						com.gutfriendly.app.admin.enums.Category category);

				List<ShopDetails> findByStatusAndShopNameContainingIgnoreCase(ShopStatus status, String shopName);

			
}
