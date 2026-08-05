package com.gutfriendly.app.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class UpdateProfileDTO {
	private String fname;
	private String lname;
	private String phoneNo;
	private String email;
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	public UpdateProfileDTO(String fname, String lname, String phoneNo, String email) {
		super();
		this.fname = fname;
		this.lname = lname;
		this.phoneNo = phoneNo;
		this.email = email;
	}
}
