package com.gutfriendly.app.inspector.model;

import java.time.LocalDateTime;
import java.util.List;

import com.gutfriendly.app.admin.enums.InspectionStatus;
import com.gutfriendly.app.admin.enums.InspectorRecommendation;
import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.admin.model.VendorDetails;

import jakarta.persistence.*;


@Entity
@Table(name = "inspection_details")
public class InspectionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inspectionId;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private VendorDetails vendor;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopDetails shop;

    @ManyToOne
    @JoinColumn(name = "inspector_id")
    private InspectorDetails inspector;

    @Column(nullable = false , name ="inspection_date")
    private LocalDateTime inspectionDate;

    private LocalDateTime completedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InspectionStatus status = InspectionStatus.ASSIGNED;

    // Overall score calculated from all performed tests
    @Column(nullable = false)
    private Double overallInspectionScore = 0.0;

    // Inspector's recommendation
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InspectorRecommendation recommendation =
            InspectorRecommendation.PENDING;

    // Inspector remarks
    @Column(length = 1000)
    private String inspectorRemarks;

    // Admin remarks after review
    @Column(length = 1000)
    private String adminRemarks;

    // Whether admin has reviewed this inspection
    @Column(nullable = false)
    private Boolean reviewedByAdmin = false;

    private LocalDateTime reviewedAt;

    @OneToMany(
            mappedBy = "inspection",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<InspectionTestResult> testResults;

    @OneToMany(
            mappedBy = "inspection",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<InspectionImages> images;

	public int getInspectionId() {
		return inspectionId;
	}

	public void setInspectionId(int inspectionId) {
		this.inspectionId = inspectionId;
	}

	public VendorDetails getVendor() {
		return vendor;
	}

	public void setVendor(VendorDetails vendor) {
		this.vendor = vendor;
	}

	public ShopDetails getShop() {
		return shop;
	}

	public void setShop(ShopDetails shop) {
		this.shop = shop;
	}

	public InspectorDetails getInspector() {
		return inspector;
	}

	public void setInspector(InspectorDetails inspector) {
		this.inspector = inspector;
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

	public String getAdminRemarks() {
		return adminRemarks;
	}

	public void setAdminRemarks(String adminRemarks) {
		this.adminRemarks = adminRemarks;
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

	public List<InspectionTestResult> getTestResults() {
		return testResults;
	}

	public void setTestResults(List<InspectionTestResult> testResults) {
		this.testResults = testResults;
	}

	public List<InspectionImages> getImages() {
		return images;
	}

	public void setImages(List<InspectionImages> images) {
		this.images = images;
	}
    
    

}

