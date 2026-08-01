package com.gutfriendly.app.admin.model;
import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.*;


@Entity
@Table(name = "user_details")
public class UserDetails {
	
	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int user_id;

	    @Column(name = "f_name", nullable = false, length = 40)
	    private String f_name;

	    @Column(name = "l_name", length = 40)
	    private String l_name;

	    @Column(name = "phone_no", length = 15)
	    private String phone_no;

	    @Column(name = "email", nullable = false, unique = true, length = 100)
	    private String email;

	    @OneToMany(cascade = CascadeType.ALL)
	    @JoinColumn(name = "address_id")
	    private List<UserAddress> address;

	    @Column(name = "joining_date")
	    private Timestamp joining_date;

	    @Column(name = "is_active")
	    private boolean is_active = true;

	    // Getters and Setters

	    public int getUser_id() {
	        return user_id;
	    }

	    public void setUser_id(int user_id) {
	        this.user_id = user_id;
	    }

	    public String getF_name() {
	        return f_name;
	    }

	    public void setF_name(String f_name) {
	        this.f_name = f_name;
	    }

	    public String getL_name() {
	        return l_name;
	    }

	    public void setL_name(String l_name) {
	        this.l_name = l_name;
	    }

	    public String getPhone_no() {
	        return phone_no;
	    }

	    public void setPhone_no(String phone_no) {
	        this.phone_no = phone_no;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
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
	}


