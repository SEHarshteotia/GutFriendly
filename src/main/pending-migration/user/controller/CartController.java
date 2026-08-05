package com.gutfriendly.app.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.user.dto.AddToCartDTO;
import com.gutfriendly.app.user.dto.CartDTO;
import com.gutfriendly.app.user.dto.UpdateCartQuantityDTO;
import com.gutfriendly.app.user.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // Add food to a user's cart
    @PostMapping("/user/{userId}/items")
    public ResponseEntity<CartDTO> addToCart(
            @PathVariable int userId,
            @RequestBody AddToCartDTO request) {

        return ResponseEntity.ok(
                cartService.addToCart(userId, request)
        );
    }

    // View the complete cart 
    @GetMapping("/user/{userId}")
    public ResponseEntity<CartDTO> getCart(
            @PathVariable int userId) {

        return ResponseEntity.ok(
                cartService.getCart(userId)
        );
    }

    // Replace the quantity of one cart item
    @PutMapping("/user/{userId}/items/{cartItemId}")
    public ResponseEntity<CartDTO> updateQuantity(
            @PathVariable int userId,
            @PathVariable int cartItemId,
            @RequestBody UpdateCartQuantityDTO request) {

        return ResponseEntity.ok(
                cartService.updateQuantity(
                        userId,
                        cartItemId,
                        request.getQuantity()
                )
        );
    }

    // Remove one item from the cart
    @DeleteMapping("/user/{userId}/items/{cartItemId}")
    public ResponseEntity<CartDTO> removeItem(
            @PathVariable int userId,
            @PathVariable int cartItemId) {

        return ResponseEntity.ok(
                cartService.removeItem(
                        userId,
                        cartItemId
                )
        );
    }

    // Remove every item from the cart
    @DeleteMapping("/user/{userId}/clear")
    public ResponseEntity<CartDTO> clearCart(
            @PathVariable int userId) {

        return ResponseEntity.ok(
                cartService.clearCart(userId)
        );
    }
}