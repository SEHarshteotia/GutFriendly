package com.gutfriendly.app.admin.dto.response;

import java.time.LocalDateTime;


public class RecentActivityResponse {
	private String activityType;
	private String message;
	private LocalDateTime time;
	
	
	public RecentActivityResponse() {
		super();
		
	}


	public RecentActivityResponse(String actitvityType, String message, LocalDateTime time) {
		super();
		this.activityType = actitvityType;
		this.message = message;
		this.time = time;
	}


	public String getActitvityType() {
		return activityType;
	}


	public void setActitvityType(String actitvityType) {
		this.activityType = actitvityType;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public LocalDateTime getTime() {
		return time;
	}


	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	
	
	

}
