package com.cdac.springBoot.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.cdac.springBoot.enums.ReviewType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;



@Entity
@Table(name="user_reviews")
public class UserReviews {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int reviewId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id",nullable=false)
	private UserDetails user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_item_id",nullable=false)
	private OrderItems orderItem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="shop_id",nullable=false)
	private ShopDetails shop;
	
	
	
	@Column(nullable = false ,name="calculated_trust_score")
	private Double calculatedTrustScore=0.0;
	
	@Column(nullable = true,name="taste_rating" )
	private Double  tasteRating;
	
	@Column(nullable = true ,  name="freshness_rating")
	private Double freshnessRating;
	
	@Column(nullable = true,name="ingredient_quality_rating")
	private Double  ingredientQualityRating;
	
	@Column(nullable = true,name="packaging_rating")
	private Double  packagingRating;
	
	@Column(nullable = true,name="temperature_rating")
	private Double  temperatureRating;
	
	@Column(nullable = true,name="ambience_rating")
	private Double ambienceRating;
	
	@Column(nullable = true,name="kitchen_hygiene_rating" )
	private Double  kitchenHygieneRating;
	
	@Column(nullable = true,name="staff_hygiene_rating")
	private Double staffHygieneRating;
	
	@Column(nullable = true,name="utensil_cleanliness_rating")
	private Double  utensilCleanlinessRating;
	
	@Column(nullable =false,length=800)
	private String review;
	
	@CreationTimestamp
	@Column(nullable=false , name = "created_at")
	private LocalDateTime createdAt;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "review_type", nullable = false, length = 20)
    private ReviewType reviewType;
	
	@Column(name = "points_awarded", nullable = false)
    private int pointsAwarded;
	
	@PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
    }



	public int getReviewId() {
		return reviewId;
	}

	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
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

	

	public Double getCalculatedTrustScore() {
		return calculatedTrustScore;
	}

	public void setCalculatedTrustScore(Double calculatedTrustScore) {
		this.calculatedTrustScore = calculatedTrustScore;
	}

	public Double getTasteRating() {
		return tasteRating;
	}

	public void setTasteRating(Double tasteRating) {
		this.tasteRating = tasteRating;
	}

	public Double getFreshnessRating() {
		return freshnessRating;
	}

	public void setFreshnessRating(Double freshnessRating) {
		this.freshnessRating = freshnessRating;
	}

	public Double getIngredientQualityRating() {
		return ingredientQualityRating;
	}

	public void setIngredientQualityRating(Double ingredientQualityRating) {
		this.ingredientQualityRating = ingredientQualityRating;
	}

	public Double getPackagingRating() {
		return packagingRating;
	}

	public void setPackagingRating(Double packagingRating) {
		this.packagingRating = packagingRating;
	}

	public Double getTemperatureRating() {
		return temperatureRating;
	}

	public void setTemperatureRating(Double temperatureRating) {
		this.temperatureRating = temperatureRating;
	}

	public Double getAmbienceRating() {
		return ambienceRating;
	}

	public void setAmbienceRating(Double ambienceRating) {
		this.ambienceRating = ambienceRating;
	}

	public Double getKitchenHygieneRating() {
		return kitchenHygieneRating;
	}

	public void setKitchenHygieneRating(Double kitchenHygieneRating) {
		this.kitchenHygieneRating = kitchenHygieneRating;
	}

	public Double getStaffHygieneRating() {
		return staffHygieneRating;
	}

	public void setStaffHygieneRating(Double staffHygieneRating) {
		this.staffHygieneRating = staffHygieneRating;
	}

	public Double getUtensilCleanlinessRating() {
		return utensilCleanlinessRating;
	}

	public void setUtensilCleanlinessRating(Double utensilCleanlinessRating) {
		this.utensilCleanlinessRating = utensilCleanlinessRating;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	
}

	


