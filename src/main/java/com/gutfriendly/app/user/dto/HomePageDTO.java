package com.gutfriendly.app.user.dto;

import java.util.List;

public class HomePageDTO {

	
	//Homepage Section
	private List<ShopCardDTO> allShops;
	private List<ShopCardDTO> trustedVendors;
	
	// RECOMMENDATION BASED ON PREV ORDERS , NEARBY, SAME CATEGORY - WILL DECIDE THE ALGO LATER.
	private List<ShopCardDTO> recommendedShops;
	
	
	//CHOICES PROVIDED BASED ON HIGHEST GUTTRUST SCORE AND BEST INSPECTION History.
	// can be a usp feature of our app
	private List<ShopCardDTO> gutFriendlyPicks;

	
	//for filtering
	private List<String> categories;
	
	//user info
	private String userName;
	private int rewardPoints;

	public HomePageDTO() {
		
	}

	public List<ShopCardDTO> getAllShops() {
		return allShops;
	}

	public void setAllShops(List<ShopCardDTO> allShops) {
		this.allShops = allShops;
	}

	public List<ShopCardDTO> getTrustedVendors() {
		return trustedVendors;
	}

	public void setTrustedVendors(List<ShopCardDTO> trustedVendors) {
		this.trustedVendors = trustedVendors;
	}

	public List<ShopCardDTO> getRecommendedShops() {
		return recommendedShops;
	}

	public void setRecommendedShops(List<ShopCardDTO> recommendedShops) {
		this.recommendedShops = recommendedShops;
	}

	public List<ShopCardDTO> getGutFriendlyPicks() {
		return gutFriendlyPicks;
	}

	public void setGutFriendlyPicks(List<ShopCardDTO> gutFriendlyPicks) {
		this.gutFriendlyPicks = gutFriendlyPicks;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getRewardPoints() {
		return rewardPoints;
	}

	public void setRewardPoints(int rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	
}
