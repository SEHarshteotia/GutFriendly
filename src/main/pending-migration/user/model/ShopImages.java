package com.gutfriendly.app.user.model;

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
public class ShopImages {
	
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int imageId;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "shop_id", nullable = false)
	    private ShopDetails shop;

	    @Column(nullable = false, length = 500)
	    private String imageUrl;

	    @Column(nullable = false)
	    private boolean primaryImage = false;

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


