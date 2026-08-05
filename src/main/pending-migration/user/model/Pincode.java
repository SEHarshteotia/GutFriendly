package com.gutfriendly.app.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
//@NoArgsConstructor
public class Pincode {
	@Id
	private String pin_code;
	
	@Column(nullable=false)
	private String city;
	
	@Column(nullable=false)
	private String state;

	public String getPin_code() {
		return pin_code;
	}

	public void setPin_code(String pin_code) {
		this.pin_code = pin_code;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Pincode() {
	}

	public Pincode(String pin_code, String city, String state) {
		super();
		this.pin_code = pin_code;
		this.city = city;
		this.state = state;
	}

	
	@Override
	public String toString() {
		return "Pincode [pin_code=" + pin_code + ", city=" + city + ", state=" + state + "]";
	}
	
}