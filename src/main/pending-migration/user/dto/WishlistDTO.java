package com.gutfriendly.app.user.dto;

import java.time.LocalDateTime;

public class WishlistDTO {

    private int wishlistId;
    private int shopId;
    private String shopName;
    private String category;
    private String locality;
    private Double gutTrustScore;
    private String imageUrl;
    private LocalDateTime savedAt;

    public WishlistDTO() {
    }

    public WishlistDTO(
            int wishlistId,
            int shopId,
            String shopName,
            String category,
            String locality,
            Double gutTrustScore,
            String imageUrl,
            LocalDateTime savedAt) {

        this.wishlistId = wishlistId;
        this.shopId = shopId;
        this.shopName = shopName;
        this.category = category;
        this.locality = locality;
        this.gutTrustScore = gutTrustScore;
        this.imageUrl = imageUrl;
        this.savedAt = savedAt;
    }

    public int getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(int wishlistId) {
        this.wishlistId = wishlistId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public Double getGutTrustScore() {
        return gutTrustScore;
    }

    public void setGutTrustScore(Double gutTrustScore) {
        this.gutTrustScore = gutTrustScore;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(LocalDateTime savedAt) {
        this.savedAt = savedAt;
    }
}