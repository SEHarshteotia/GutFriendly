package com.gutfriendly.app.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.user.dto.WishlistDTO;
import com.gutfriendly.app.user.service.WishlistService;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    // Adds a shop to the user's wishlist.
    @PostMapping("/user/{userId}/shop/{shopId}")
    public ResponseEntity<WishlistDTO> addToWishlist(
            @PathVariable int userId,
            @PathVariable int shopId) {

        return ResponseEntity.ok(
                wishlistService.addToWishlist(
                        userId,
                        shopId
                )
        );
    }

    
    // Returns all shops saved by the user.
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WishlistDTO>> getWishlist(
            @PathVariable int userId) {

        return ResponseEntity.ok(
                wishlistService.getWishlist(userId)
        );
    }
    
    
    
 // Removes a shop from the user's wishlist.
    @DeleteMapping("/user/{userId}/shop/{shopId}")
    public ResponseEntity<String> removeFromWishlist(
            @PathVariable int userId,
            @PathVariable int shopId) {

        wishlistService.removeFromWishlist(
                userId,
                shopId
        );

        return ResponseEntity.ok(
                "Shop removed from wishlist successfully"
        );
    }
    
    
 // Checks whether a shop is already wishlisted.
    @GetMapping("/user/{userId}/shop/{shopId}/status")
    public ResponseEntity<Boolean> isShopWishlisted(
            @PathVariable int userId,
            @PathVariable int shopId) {

        return ResponseEntity.ok(
                wishlistService.isShopWishlisted(
                        userId,
                        shopId
                )
        );
    }
}