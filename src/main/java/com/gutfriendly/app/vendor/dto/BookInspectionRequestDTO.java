package com.gutfriendly.app.vendor.dto;

import java.time.LocalDateTime;

public class BookInspectionRequestDTO {

	private LocalDateTime inspectionDate;

	public LocalDateTime getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(LocalDateTime inspectionDate) {
		this.inspectionDate = inspectionDate;
	}
}
