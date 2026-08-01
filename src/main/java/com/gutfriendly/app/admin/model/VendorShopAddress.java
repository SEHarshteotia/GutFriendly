package com.gutfriendly.app.admin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vendor_shop_address")
public class VendorShopAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private int addressId;

    @Column(nullable = false, length = 100)
    private String locality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_code", nullable = false)
    private Pincode pinCode;
    
    @Column(nullable = false, length = 50)
    private String shopNumber;

    // Getters and Setters

    public int getAddressId() {
        return addressId;
    }

    public String getShopNumber() {
		return shopNumber;
	}

	public void setShopNumber(String shopNumber) {
		this.shopNumber = shopNumber;
	}

	public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public Pincode getPinCode() {
        return pinCode;
    }

    public void setPinCode(Pincode pinCode) {
        this.pinCode = pinCode;
    }
}