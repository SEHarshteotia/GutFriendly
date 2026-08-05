package com.gutfriendly.app.user.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gutfriendly.app.user.enums.Category;
import com.gutfriendly.app.user.model.ShopDetails;

public interface ShopDetailsRepository extends JpaRepository<ShopDetails, Integer> {
	
	List<ShopDetails> findByOrderByFinalGutTrustScoreDesc();
	
	List<ShopDetails> findByShopNameContainingIgnoreCase(String keyword);
	
	List<ShopDetails> findByCategory(Category category);
	
	
}
