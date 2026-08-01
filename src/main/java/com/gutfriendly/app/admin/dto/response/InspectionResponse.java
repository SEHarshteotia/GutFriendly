package com.gutfriendly.app.admin.dto.response;

import java.time.LocalDateTime;

import com.gutfriendly.app.admin.enums.InspectionStatus;
import com.gutfriendly.app.admin.enums.InspectorRecommendation;

public class InspectionResponse {
	
	    private int inspectionId;
	    private int vendorId;
        private int shopId;
	    private String shopName;
	    private String vendorName;
        private int inspectorId;
	    private String inspectorName;
        private LocalDateTime inspectionDate;
        private LocalDateTime completedAt;
        private InspectionStatus status;
        private Double overallInspectionScore;
        private InspectorRecommendation recommendation;
        private String inspectorRemarks;
        private Boolean reviewedByAdmin;
        private LocalDateTime reviewedAt;
        private String adminRemarks;
        
        
		public InspectionResponse() {
			super();
			
		}
		
		public InspectionResponse(int inspectionId, int shopId, String shopName, int inspectorId, String inspectorName,
				LocalDateTime inspectionDate, LocalDateTime completedAt, InspectionStatus status,
				Double overallInspectionScore, InspectorRecommendation recommendation, String inspectorRemarks,
				Boolean reviewedByAdmin, LocalDateTime reviewedAt,int vendorId,String adminRemarks,String vendorName) {
			super();
			this.inspectionId = inspectionId;
			this.shopId = shopId;
			this.shopName = shopName;
			this.inspectorId = inspectorId;
			this.inspectorName = inspectorName;
			this.inspectionDate = inspectionDate;
			this.completedAt = completedAt;
			this.status = status;
			this.overallInspectionScore = overallInspectionScore;
			this.recommendation = recommendation;
			this.inspectorRemarks = inspectorRemarks;
			this.reviewedByAdmin = reviewedByAdmin;
			this.reviewedAt = reviewedAt;
			this.vendorId= vendorId;
			this.adminRemarks=adminRemarks;
			this.vendorName=vendorName;
		}
		
		
		
		
		public String getVendorName() {
			return vendorName;
		}

		public void setVendorName(String vendorName) {
			this.vendorName = vendorName;
		}

		public String getAdminRemarks() {
			return adminRemarks;
		}

		public void setAdminRemarks(String adminRemarks) {
			this.adminRemarks = adminRemarks;
		}

		public int getVendorId() {
			return vendorId;
		}

		public void setVendorId(int vendorId) {
			this.vendorId = vendorId;
		}

		public int getInspectionId() {
			return inspectionId;
		}
		public void setInspectionId(int inspectionId) {
			this.inspectionId = inspectionId;
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
		public int getInspectorId() {
			return inspectorId;
		}
		public void setInspectorId(int inspectorId) {
			this.inspectorId = inspectorId;
		}
		public String getInspectorName() {
			return inspectorName;
		}
		public void setInspectorName(String inspectorName) {
			this.inspectorName = inspectorName;
		}
		public LocalDateTime getInspectionDate() {
			return inspectionDate;
		}
		public void setInspectionDate(LocalDateTime inspectionDate) {
			this.inspectionDate = inspectionDate;
		}
		public LocalDateTime getCompletedAt() {
			return completedAt;
		}
		public void setCompletedAt(LocalDateTime completedAt) {
			this.completedAt = completedAt;
		}
		public InspectionStatus getStatus() {
			return status;
		}
		public void setStatus(InspectionStatus status) {
			this.status = status;
		}
		public Double getOverallInspectionScore() {
			return overallInspectionScore;
		}
		public void setOverallInspectionScore(Double overallInspectionScore) {
			this.overallInspectionScore = overallInspectionScore;
		}
		public InspectorRecommendation getRecommendation() {
			return recommendation;
		}
		public void setRecommendation(InspectorRecommendation recommendation) {
			this.recommendation = recommendation;
		}
		public String getInspectorRemarks() {
			return inspectorRemarks;
		}
		public void setInspectorRemarks(String inspectorRemarks) {
			this.inspectorRemarks = inspectorRemarks;
		}
		public Boolean getReviewedByAdmin() {
			return reviewedByAdmin;
		}
		public void setReviewedByAdmin(Boolean reviewedByAdmin) {
			this.reviewedByAdmin = reviewedByAdmin;
		}
		public LocalDateTime getReviewedAt() {
			return reviewedAt;
		}
		public void setReviewedAt(LocalDateTime reviewedAt) {
			this.reviewedAt = reviewedAt;
		}
        

}
