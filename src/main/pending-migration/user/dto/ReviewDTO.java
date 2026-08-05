package com.gutfriendly.app.user.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.gutfriendly.app.user.enums.ReviewKeyword;
import com.gutfriendly.app.user.enums.ReviewType;

public class ReviewDTO {

    private int reviewId;
    private int orderId;
    private int shopId;
    private String shopName;
    private String userName;
    private int rating;
    private String comment;
    private List<ReviewKeyword> keywords;
    private ReviewType reviewType;
    private int pointsAwarded;
    private LocalDateTime createdAt;

    public ReviewDTO() {
    }

    public ReviewDTO(
            int reviewId,
            int orderId,
            int shopId,
            String shopName,
            String userName,
            int rating,
            String comment,
            List<ReviewKeyword> keywords,
            ReviewType reviewType,
            int pointsAwarded,
            LocalDateTime createdAt) {

        this.reviewId = reviewId;
        this.orderId = orderId;
        this.shopId = shopId;
        this.shopName = shopName;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.keywords = keywords;
        this.reviewType = reviewType;
        this.pointsAwarded = pointsAwarded;
        this.createdAt = createdAt;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ReviewKeyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<ReviewKeyword> keywords) {
        this.keywords = keywords;
    }

    public ReviewType getReviewType() {
        return reviewType;
    }

    public void setReviewType(ReviewType reviewType) {
        this.reviewType = reviewType;
    }

    public int getPointsAwarded() {
        return pointsAwarded;
    }

    public void setPointsAwarded(int pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}