package com.gutfriendly.app.admin.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "shop_images")
public class FoodImages {
	
	public class ShopImages {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int imageId;
	    
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "shop_id", nullable = false)
	    private ShopDetails shop;

	    @Column(name = "image_url", nullable = false, length = 500)
	    private String imageUrl;

	    @Column(name = "is_primary", nullable = false)
	    private boolean primaryImage = false;
	    
	    @Column(name = "uploaded_at", nullable = false)
	    private LocalDateTime uploadedAt;

	    public LocalDateTime getUploadedAt() {
			return uploadedAt;
		}

		public void setUploadedAt(LocalDateTime uploadedAt) {
			this.uploadedAt = uploadedAt;
		}

		public int getImageId() {
			return imageId;
		}

		public void setImageId(int imageId) {
			this.imageId = imageId;
		}

		public ShopDetails getShop() {
			return shop;
		}

		public void setShop(ShopDetails shop) {
			this.shop = shop;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}

		public boolean isPrimaryImage() {
			return primaryImage;
		}

		public void setPrimaryImage(boolean primaryImage) {
			this.primaryImage = primaryImage;
		}

		

	}
	}
