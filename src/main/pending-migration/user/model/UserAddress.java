package com.gutfriendly.app.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gutfriendly.app.user.enums.AddressType;

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
import lombok.Data;


@Entity
@Table(name = "user_address")
@Data
public class UserAddress {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer address_Id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private UserDetails user;

	@Column(nullable = false)
	private String locality;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "Enum('Home','Work','Other')", nullable = false)
	private AddressType address_type = AddressType.Home;

	@ManyToOne
	@JoinColumn(name = "pin_code")
	private Pincode pincode;

	
	
	
	//getter setter
	public Integer getAddress_Id() {
		return address_Id;
	}

	public void setAddress_Id(Integer address_Id) {
		this.address_Id = address_Id;
	}

	public String getLocality() {
		return locality;
	}

	public UserDetails getUser() {
		return user;
	}

	public void setUser(UserDetails user) {
		this.user = user;
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
	
	
	public UserAddress() {
	}

	public UserAddress(Integer address_Id, String locality, AddressType address_type, Pincode pincode) {
		super();
		this.address_Id = address_Id;
		this.locality = locality;
		this.address_type = address_type;
		this.pincode = pincode;
	}

	@Override
	public String toString() {
		return "UserAddress [address_Id=" + address_Id + ", locality=" + locality + ", address_type=" + address_type
				+ ", pincode=" + pincode + "]";
	}
	
}	
	
	