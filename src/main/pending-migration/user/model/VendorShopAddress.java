package com.gutfriendly.app.user.model;
import jakarta.persistence.*;

@Entity
@Table(name="vendor_shop_address")
public class VendorShopAddress {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int address_id;
	
	
	@OneToOne
	@JoinColumn(name = "vendor_id")
	private VendorDetails vendor;
	
	@Column(nullable = false)
	private String locality;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "pin_code",nullable = false)
	private Pincode pin_code;

	//Getters and Setters
	public int getAddress_id() {
		return address_id;
	}

	public void setAddress_id(int address_id) {
		this.address_id = address_id;
	}

	public VendorDetails getVendor() {
		return vendor;
	}

	public void setVendor(VendorDetails vendor) {
		this.vendor = vendor;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public Pincode getPin_code() {
		return pin_code;
	}

	public void setPin_code(Pincode pin_code) {
		this.pin_code = pin_code;
	}
	
}



