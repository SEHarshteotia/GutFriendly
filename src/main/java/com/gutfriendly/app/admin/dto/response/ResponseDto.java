package com.gutfriendly.app.admin.dto.response;

public class ResponseDto {
	private String message;
	private String status;
	public ResponseDto(String message , String status){
		this.message=message;
		this.status=status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
