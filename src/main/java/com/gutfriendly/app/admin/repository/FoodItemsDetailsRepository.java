package com.gutfriendly.app.admin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.admin.model.FoodItemsDetails;
import com.gutfriendly.app.admin.model.ShopDetails;

public interface FoodItemsDetailsRepository extends JpaRepository<FoodItemsDetails, Integer> {

	List<FoodItemsDetails> findByShopOrderByFoodCategoryAscFoodNameAsc(ShopDetails shop);

	List<FoodItemsDetails> findByShopAndAvailableTrueOrderByFoodCategoryAscFoodNameAsc(ShopDetails shop);

	Optional<FoodItemsDetails> findByFoodIdAndShop(int foodId, ShopDetails shop);

	Optional<FoodItemsDetails> findByFoodId(int foodId);

	List<FoodItemsDetails> findByFoodNameContainingIgnoreCase(String keyword);

	Page<FoodItemsDetails> findByShopShopIdAndAvailableTrue(int shopId, Pageable pageable);

	Page<FoodItemsDetails> findByFoodNameContainingIgnoreCaseAndAvailableTrue(String keyword, Pageable pageable);
}
