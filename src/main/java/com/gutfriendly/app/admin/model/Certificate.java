package com.gutfriendly.app.admin.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name="certificate")
public class Certificate {
	    
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int certificate_id;
	    
	    @ManyToOne
	    @JoinColumn(name="vendor_id")
	    private VendorDetails vendor;

	    @ManyToOne
	    @JoinColumn(name="shop_id")
	    private ShopDetails shop;

	    @Column(nullable = false , name ="certificate_number")
	    private String certificateNumber;
	    
	    @Column(nullable=false,name="issue_date")
	    private LocalDateTime issueDate;

	    @Column(nullable=false,name="expiry_date")
	    private LocalDateTime expiryDate;

	    @Column(nullable=false)
	    private boolean active;


	    public int getCertificate_id() {
			return certificate_id;
		}

		public void setCertificate_id(int certificate_id) {
			this.certificate_id = certificate_id;
		}

		public VendorDetails getVendor() {
			return vendor;
		}

		public void setVendor(VendorDetails vendor) {
			this.vendor = vendor;
		}

		public ShopDetails getShop() {
			return shop;
		}

		public void setShop(ShopDetails shop) {
			this.shop = shop;
		}

		public String getCertificate_number() {
			return certificateNumber;
		}

		public void setCertificate_number(String certificate_number) {
			this.certificateNumber = certificate_number;
		}

		public LocalDateTime getIssue_date() {
			return issueDate;
		}

		public void setIssue_date(LocalDateTime issue_date) {
			this.issueDate = issue_date;
		}

		public LocalDateTime getExpiry_date() {
			return expiryDate;
		}

		public void setExpiry_date(LocalDateTime expiry_date) {
			this.expiryDate = expiry_date;
		}

		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

		

	   
	   

	}


