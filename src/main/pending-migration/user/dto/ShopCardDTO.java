package com.gutfriendly.app.user.dto;

public class ShopCardDTO {

	private int shopId;
	private String shopName;
	private String category;
	private String locality;
	private double gutTrustScore;
	private String imageUrl;
	
	
	
	public ShopCardDTO(int shopId, 
			String shopName, 
			String category, 
			String locality, 
			double gutTrustScore,
			String imageUrl){

		this.shopId = shopId;
		this.shopName = shopName;
		this.category = category;
		this.locality = locality;
		this.gutTrustScore = gutTrustScore;
		this.imageUrl = imageUrl;
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



	public double getGutTrustScore() {
		return gutTrustScore;
	}



	public void setGutTrustScore(double gutTrustScore) {
		this.gutTrustScore = gutTrustScore;
	}



	public String getImageUrl() {
		return imageUrl;
	}



	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	
	
}
