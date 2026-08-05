package com.gutfriendly.app.user.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gutfriendly.app.user.enums.ReviewKeyword;
import com.gutfriendly.app.user.enums.ReviewType;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "shop_reviews",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_review_order",
                        columnNames = "order_id"
                )
        }
)
public class ShopReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    // One order can be reviewed only once.
     
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private CustomerOrder order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserDetails user;

    
//      Review belongs to the exact shop from the order,
//      not to all shops belonging to the vendor.
     
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopDetails shop;

    @Column(nullable = false)
    private int rating;

    @Column(length = 1000)
    private String comment;

    @ElementCollection
    @CollectionTable(
            name = "review_keywords",
            joinColumns = @JoinColumn(name = "review_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "keyword", length = 40)
    private List<ReviewKeyword> keywords = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "review_type", nullable = false, length = 20)
    private ReviewType reviewType;

    @Column(name = "points_awarded", nullable = false)
    private int pointsAwarded;
    
    
    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(name = "reward_granted", nullable = false)
    private boolean rewardGranted = false;

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public CustomerOrder getOrder() {
        return order;
    }

    public void setOrder(CustomerOrder order) {
        this.order = order;
    }

    public UserDetails getUser() {
        return user;
    }

    public void setUser(UserDetails user) {
        this.user = user;
    }

    public ShopDetails getShop() {
        return shop;
    }

    public void setShop(ShopDetails shop) {
        this.shop = shop;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

	public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public boolean isRewardGranted() {
        return rewardGranted;
    }

    public void setRewardGranted(boolean rewardGranted) {
        this.rewardGranted = rewardGranted;
    }
}