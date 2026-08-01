package com.gutfriendly.app.admin.dto.response;

import java.time.LocalDateTime;

public class UpcomingInspectionResponse {

    private String shopName;
    private String vendorName;
    private LocalDateTime inspectionDate;
    private String status;
    
    public UpcomingInspectionResponse() {
		super();
		
	}
    
	public UpcomingInspectionResponse(String shopName, String vendorName, LocalDateTime inspectionDate, String status) {
		super();
		this.shopName = shopName;
		this.vendorName = vendorName;
		this.inspectionDate = inspectionDate;
		this.status = status;
	}

	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public LocalDateTime getInspectionDate() {
		return inspectionDate;
	}
	public void setInspectionDate(LocalDateTime inspectionDate) {
		this.inspectionDate = inspectionDate;
	}
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
    
    

}
