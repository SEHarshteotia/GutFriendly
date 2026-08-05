package com.gutfriendly.app.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.user.model.FoodItemsDetails;

public interface FoodItemsDetailsRepository
        extends JpaRepository<FoodItemsDetails, Integer> {

    Page<FoodItemsDetails> findByShopShopIdAndAvailableTrue(
            int shopId,
            Pageable pageable
    );

    Page<FoodItemsDetails>
            findByFoodNameContainingIgnoreCaseAndAvailableTrue(
                    String keyword,
                    Pageable pageable
            );
}