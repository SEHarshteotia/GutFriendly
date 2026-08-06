package com.gutfriendly.app.admin.model;
import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "vendor_details")
public class VendorDetails {
	   
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int vendorId;

	    @Column(name = "f_name", nullable = false, length = 40)
	    private String firstName;

	    @Column(name = "m_name", length = 40)
	    private String middleName;

	    @Column(name = "l_name", nullable = false, length = 40)
	    private String lastName;

	    @Column(name = "phone_no", nullable = false, length = 40, unique = true)
	    private String phoneNo;

	    @Column(nullable = false)
	    private String password;

	    @Column(name = "email", length = 100,unique = true)
	    private String email;

	    @Column(name = "adhar_no", nullable = true, unique = true)
	    private String adharNo;

	    @Column(name = "pan_no", nullable = true, length = 20, unique = true)
	    private String panNo;
	    
		@Column(name = "joining_date")
	    private LocalDateTime joiningDate;

	    @Column(name = "is_active")
	    private boolean isActive = true;
	    
	    //Add shops 
	    @OneToMany(mappedBy="vendor")
	    private List<ShopDetails> shops;

	    // Getters and Setters
	    public int getVendorId() {
			return vendorId;
		}

		public void setVendorId(int vendorId) {
			this.vendorId = vendorId;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getMiddleName() {
			return middleName;
		}

		public void setMiddleName(String middleName) {
			this.middleName = middleName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getPhoneNo() {
			return phoneNo;
		}

		public void setPhoneNo(String phoneNo) {
			this.phoneNo = phoneNo;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getAdharNo() {
			return adharNo;
		}

		public void setAdharNo(String adharNo) {
			this.adharNo = adharNo;
		}

		public String getPanNo() {
			return panNo;
		}

		public void setPanNo(String panNo) {
			this.panNo = panNo;
		}

		

		public LocalDateTime getJoiningDate() {
			return joiningDate;
		}

		public void setJoiningDate(LocalDateTime joiningDate) {
			this.joiningDate = joiningDate;
		}

		public boolean isActive() {
			return isActive;
		}

		public void setActive(boolean isActive) {
			this.isActive = isActive;
		}

		public List<ShopDetails> getShops() {
			return shops;
		}

		public void setShops(List<ShopDetails> shops) {
			this.shops = shops;
		}



	    
	  
	    

}