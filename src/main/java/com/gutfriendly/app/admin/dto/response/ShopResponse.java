package com.gutfriendly.app.admin.dto.response;

import java.time.LocalDateTime;

import com.gutfriendly.app.admin.enums.Category;
import com.gutfriendly.app.admin.enums.ServiceAvailabilityStatus;
import com.gutfriendly.app.admin.enums.ShopStatus;

public class ShopResponse {
	private int shopId;
	private String shopName;
	private String gstNo;
	private Category category;

	private ShopStatus status;

	private ServiceAvailabilityStatus serviceAvailabilityStatus;

	private Double finalGutTrustScore;

	private Boolean blocked = false;
	
	

	private LocalDateTime createdAt;
	
	private String adminRemarks;
	
	
	


	public String getAdminRemarks() {
		return adminRemarks;
	}

	public void setAdminRemarks(String adminRemarks) {
		this.adminRemarks = adminRemarks;
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

	public String getGstNo() {
		return gstNo;
	}

	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public ShopStatus getStatus() {
		return status;
	}

	public void setStatus(ShopStatus status) {
		this.status = status;
	}

	public ServiceAvailabilityStatus getServiceAvailabilityStatus() {
		return serviceAvailabilityStatus;
	}

	public void setServiceAvailabilityStatus(ServiceAvailabilityStatus serviceAvailabilityStatus) {
		this.serviceAvailabilityStatus = serviceAvailabilityStatus;
	}

	public Double getFinalGutTrustScore() {
		return finalGutTrustScore;
	}

	public void setFinalGutTrustScore(Double finalGutTrustScore) {
		this.finalGutTrustScore = finalGutTrustScore;
	}

	public Boolean getBlocked() {
		return blocked;
	}

	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public ShopResponse() {
		super();
	}

	public ShopResponse(int shopId, String shopName, String gstNo, Category category, ShopStatus status,
			ServiceAvailabilityStatus serviceAvailabilityStatus, Double finalGutTrustScore, Boolean blocked,
			LocalDateTime createdAt, String adminRemarks) {
		super();
		this.shopId = shopId;
		this.shopName = shopName;
		this.gstNo = gstNo;
		this.category = category;
		this.status = status;
		this.serviceAvailabilityStatus = serviceAvailabilityStatus;
		this.finalGutTrustScore = finalGutTrustScore;
		this.blocked = blocked;
		this.createdAt = createdAt;
		this.adminRemarks=adminRemarks;
	}
	
	

}
