package com.gutfriendly.app.admin.dto.response;

public class DashboardSummaryDto {
	private double averageGutTrustScore ; 
	private long  totalVerfiedVendors;
	private long  activeInspections;
	private long  pendingVendorApprovals;
    private long  pendingComplaints;
     private long expiringCertificates;
	 public DashboardSummaryDto() {
		super();
	}
	 
	public DashboardSummaryDto(double averageGutTrustScore, long totalVerfiedVendors, long activeInspections,
			long pendingVendorApprovals, long pendingComplaints, long expiringCertificates) {
		super();
		this.averageGutTrustScore = averageGutTrustScore;
		this.totalVerfiedVendors = totalVerfiedVendors;
		this.activeInspections = activeInspections;
		this.pendingVendorApprovals = pendingVendorApprovals;
		this.pendingComplaints = pendingComplaints;
		this.expiringCertificates = expiringCertificates;
	 }

	public double getAverageGutTrustScore() {
		return averageGutTrustScore;
	}

	public void setAverageGutTrustScore(double averageGutTrustScore) {
		this.averageGutTrustScore = averageGutTrustScore;
	}

	public long getTotalVerfiedVendors() {
		return totalVerfiedVendors;
	}

	public void setTotalVerfiedVendors(long totalVerfiedVendors) {
		this.totalVerfiedVendors = totalVerfiedVendors;
	}

	public long getActiveInspections() {
		return activeInspections;
	}

	public void setActiveInspections(long activeInspections) {
		this.activeInspections = activeInspections;
	}

	public long getPendingVendorApprovals() {
		return pendingVendorApprovals;
	}

	public void setPendingVendorApprovals(long pendingVendorApprovals) {
		this.pendingVendorApprovals = pendingVendorApprovals;
	}

	public long getPendingComplaints() {
		return pendingComplaints;
	}

	public void setPendingComplaints(long pendingComplaints) {
		this.pendingComplaints = pendingComplaints;
	}

	public long getExpiringCertificates() {
		return expiringCertificates;
	}

	public void setExpiringCertificates(long expiringCertificates) {
		this.expiringCertificates = expiringCertificates;
	}
	
	 
     

}
