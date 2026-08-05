package com.gutfriendly.app.user.model;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "User_Details")
public class UserDetails {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int user_id;

	@Column(name = "f_name", nullable = false, length = 40)
	private String fname;
	

	@Column(name = "l_name", nullable = false, length = 40)
	private String lname;
	
	@Column(name = "phone_no", nullable = false, length = 40)
	private String phoneNo;
	
	@Column(name = "email", nullable = false, unique = true, length = 40)
	private String email;
	
	@Column(name = "password", nullable = false, length = 40)
	private String password;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<UserAddress> address;
	
	@Column(name = "joining_date", nullable = false, length = 40)
	private Timestamp joining_date;
	
	@Column(name = "is_active", nullable = false, length = 40)
	private boolean is_active;

	@Column(nullable = false)
	private boolean trustedUser = false;
	
	
	@Column(name = "reward_points", nullable = false)
	private int rewardPoints = 0;
	
	
	
	//getter setter
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<UserAddress> getAddress() {
		return address;
	}

	public void setAddress(List<UserAddress> address) {
		this.address = address;
	}

	public Timestamp getJoining_date() {
		return joining_date;
	}

	public void setJoining_date(Timestamp joining_date) {
		this.joining_date = joining_date;
	}

	public boolean isIs_active() {
		return is_active;
	}

	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}

	public boolean isTrustedUser() {
		return trustedUser;
	}

	public void setTrustedUser(boolean trustedUser) {
		this.trustedUser = trustedUser;
	}
	
	
	public int getRewardPoints() {
	    return rewardPoints;
	}

	public void setRewardPoints(int rewardPoints) {
	    this.rewardPoints = rewardPoints;
	}
	
	
}