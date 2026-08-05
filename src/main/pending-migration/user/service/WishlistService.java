package com.gutfriendly.app.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gutfriendly.app.user.dto.WishlistDTO;
import com.gutfriendly.app.user.model.ShopDetails;
import com.gutfriendly.app.user.model.ShopImages;
import com.gutfriendly.app.user.model.UserDetails;
import com.gutfriendly.app.user.model.Wishlist;
import com.gutfriendly.app.user.repository.ShopDetailsRepository;
import com.gutfriendly.app.user.repository.UserRepo;
import com.gutfriendly.app.user.repository.WishlistRepository;

import com.gutfriendly.app.user.exception.BadRequestException;
import com.gutfriendly.app.user.exception.ConflictException;
import com.gutfriendly.app.user.exception.ResourceNotFoundException;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ShopDetailsRepository shopRepo;

    // Adds one shop to the user's wishlist.
    @Transactional
    public WishlistDTO addToWishlist(
            int userId,
            int shopId) {

        UserDetails user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        ShopDetails shop = shopRepo.findById(shopId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Shop not found"));

        if (wishlistRepo.existsByUserIdAndShopId(
                userId,
                shopId)) {

            throw new ConflictException(
                    "Shop is already present in wishlist"
            );
        }

        Wishlist wishlist = new Wishlist();

        wishlist.setUser(user);
        wishlist.setShop(shop);

        Wishlist savedWishlist =
                wishlistRepo.save(wishlist);

        return convertToDTO(savedWishlist);
    }

    // Returns all saved shops of 1 user.
    @Transactional(readOnly = true)
    public List<WishlistDTO> getWishlist(
            int userId) {

        if (!userRepo.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        return wishlistRepo
                .findWishlistByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Removes 1 shop from the user's wishlist.
    @Transactional
    public void removeFromWishlist(
            int userId,
            int shopId) {

        Wishlist wishlist = wishlistRepo
                .findByUserIdAndShopId(
                        userId,
                        shopId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Shop is not present in wishlist"
                        ));

        wishlistRepo.delete(wishlist);
    }

    // Checks whether the shop is already saved.
    @Transactional(readOnly = true)
    public boolean isShopWishlisted(
            int userId,
            int shopId) {

        if (!userRepo.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        if (!shopRepo.existsById(shopId)) {
            throw new ResourceNotFoundException("Shop not found");
        }

        return wishlistRepo
                .existsByUserIdAndShopId(
                        userId,
                        shopId
                );
    }

    // Converts Wishlist entity into frontend response DTO.
    private WishlistDTO convertToDTO(
            Wishlist wishlist) {

        ShopDetails shop = wishlist.getShop();

        String locality = null;
        String imageUrl = null;

        if (shop.getAddress_id() != null) {
            locality =
                    shop.getAddress_id().getLocality();
        }

        if (shop.getImages() != null &&
                !shop.getImages().isEmpty()) {

            for (ShopImages image : shop.getImages()) {
                imageUrl = image.getImageUrl();
                break;
            }
        }

        return new WishlistDTO(
                wishlist.getWishlistId(),
                shop.getShopId(),
                shop.getShopName(),
                shop.getCategory().name(),
                locality,
                shop.getFinalGutTrustScore(),
                imageUrl,
                wishlist.getCreatedAt()
        );
    }
}