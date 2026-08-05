package com.gutfriendly.app.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.user.model.CartItem;

public interface CartItemRepository
        extends JpaRepository<CartItem, Integer> {

    Optional<CartItem> findByCartCartIdAndFoodFoodId(
            int cartId,
            int foodId
    );
}