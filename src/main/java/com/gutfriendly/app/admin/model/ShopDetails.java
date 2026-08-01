package com.gutfriendly.app.admin.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.gutfriendly.app.admin.enums.Category;
import com.gutfriendly.app.admin.enums.ServiceAvailabilityStatus;

import com.gutfriendly.app.admin.enums.ShopStatus;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;



@Entity
@Table(name="shop_details")
public class ShopDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int shopId;
	
	@Column(nullable = false, length = 100)
	private String shopName;
	

	@Column(nullable = false, length = 100,unique=true)
	private String gstNo;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Category category=Category.RESTAURANT;
	
	@Column(nullable = false)
	private Double userTrustScore = 0.0;
	
	@Column(nullable = false)
	private Double inspectionTrustScore = 0.0;
	
	@Column(nullable = false)
	private Double finalGutTrustScore = 0.0;
	
	@OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
	private List<ShopImages> images;
	
	@ManyToOne()
	@JoinColumn(name = "vendor_id")
	private VendorDetails vendor;
	
	@OneToOne()
	@JoinColumn(name = "address_id",nullable = true)
	private VendorShopAddress address_id ;
	
	@Column(nullable = false)
	private LocalDateTime lastCalculatedAt =  LocalDateTime.now();
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ShopStatus status = ShopStatus.PENDING;
	
	@Column(nullable = false)
	private Boolean blocked = false;
	
	@Column(length=500)
	private String adminRemarks;
	
	
	private LocalDateTime verifiedAt;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ServiceAvailabilityStatus serviceAvailabilityStatus =
	        ServiceAvailabilityStatus.NOT_SERVICEABLE;
	
	
	
	public String getAdminRemarks() {
		return adminRemarks;
	}

	public void setAdminRemarks(String adminRemarks) {
		this.adminRemarks = adminRemarks;
	}

	public ShopStatus getStatus() {
		return status;
	}

	public void setStatus(ShopStatus status) {
		this.status = status;
	}

	public Boolean getBlocked() {
		return blocked;
	}

	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}

	

	public LocalDateTime getVerifiedAt() {
		return verifiedAt;
	}

	public void setVerifiedAt(LocalDateTime verifiedAt) {
		this.verifiedAt = verifiedAt;
	}

	public ServiceAvailabilityStatus getServiceAvailabilityStatus() {
		return serviceAvailabilityStatus;
	}

	public void setServiceAvailabilityStatus(ServiceAvailabilityStatus serviceAvailabilityStatus) {
		this.serviceAvailabilityStatus = serviceAvailabilityStatus;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	
	
	public List<ShopImages> getImages() {
		return images;
	}

	public void setImages(List<ShopImages> images) {
		this.images = images;
	}

	public LocalDateTime getLastCalculatedAt() {
		return lastCalculatedAt;
	}

	public void setLastCalculatedAt(LocalDateTime lastCalculatedAt) {
		this.lastCalculatedAt = lastCalculatedAt;
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

	public Double getUserTrustScore() {
		return userTrustScore;
	}

	public void setUserTrustScore(Double userTrustScore) {
		this.userTrustScore = userTrustScore;
	}

	public Double getInspectionTrustScore() {
		return inspectionTrustScore;
	}

	public void setInspectionTrustScore(Double inspectionTrustScore) {
		this.inspectionTrustScore = inspectionTrustScore;
	}

	public Double getFinalGutTrustScore() {
		return finalGutTrustScore;
	}

	public void setFinalGutTrustScore(Double finalGutTrustScore) {
		this.finalGutTrustScore = finalGutTrustScore;
	}

	

	public VendorDetails getVendor() {
		return vendor;
	}

	public void setVendor(VendorDetails vendor) {
		this.vendor = vendor;
	}

	public VendorShopAddress getAddress_id() {
		return address_id;
	}

	public void setAddress_id(VendorShopAddress address_id) {
		this.address_id = address_id;
	}

	

}
