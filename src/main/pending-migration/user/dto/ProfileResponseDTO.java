package com.gutfriendly.app.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileResponseDTO {
	
	private int user_id;
	private String fname;
	private String lname;
	private String phoneNo;
	private String email;
	private boolean trustedUser;
	
	
	
	public int getUser_id() {
		return user_id;
	}



	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}



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



	public boolean isTrustedUser() {
		return trustedUser;
	}



	public void setTrustedUser(boolean trustedUser) {
		this.trustedUser = trustedUser;
	}



	public ProfileResponseDTO(int user_id, String fname, String lname, String phoneNo, String email,boolean trustedUser) {
		super();
		this.user_id = user_id;
		this.fname = fname;
		this.lname = lname;
		this.phoneNo = phoneNo;
		this.email = email;
		this.trustedUser = trustedUser;
	}



	@Override
	public String toString() {
		return "ProfileResponseDTO [user_id=" + user_id + ", fname=" + fname + ", lname=" + lname + ", phoneNo="
				+ phoneNo + ", email=" + email + ", trustedUser=" + trustedUser + "]";
	}

}
