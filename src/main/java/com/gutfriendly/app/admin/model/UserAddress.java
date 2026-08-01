package com.gutfriendly.app.admin.model;

import com.gutfriendly.app.admin.enums.AddressType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;



@Entity
@Table
public class UserAddress {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int address_Id;


	@Column(nullable = false)
	private String locality;

	@Enumerated(EnumType.STRING)
	@Column( nullable = false)
	private AddressType address_type = AddressType.HOME;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "pin_code")
	private Pincode pincode;

	public int getAddress_Id() {
		return address_Id;
	}

	public void setAddress_Id(int address_Id) {
		this.address_Id = address_Id;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public AddressType getAddress_type() {
		return address_type;
	}

	public void setAddress_type(AddressType address_type) {
		this.address_type = address_type;
	}

	public Pincode getPincode() {
		return pincode;
	}

	public void setPincode(Pincode pincode) {
		this.pincode = pincode;
	}

}
