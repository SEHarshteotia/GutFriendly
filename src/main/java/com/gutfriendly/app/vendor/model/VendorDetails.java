package com.gutfriendly.app.vendor.model;

import java.sql.Timestamp;

import com.gutfriendly.app.vendor.status.VendorStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vendor_details")
@Data
@NoArgsConstructor
public class VendorDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer vendor_id;

	@Column(nullable = false, length = 40)
	private String fName;

	@Column(length = 40)
	private String mName;

	@Column(nullable = false, length = 40)
	private String lName;

	@Column(nullable = false, unique = true, length = 10)
	private String phoneNo;

	@Column(nullable = false)
	private String password;

	@Column(unique = true, length = 100)
	private String email;

	@Column(unique = true, length = 12)
	private String aadharNo;

	@Column(unique = true, length = 10)
	private String panNo;

	@OneToOne(mappedBy = "vendor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private VendorAddress address;

	private Timestamp joining_date;

	private Boolean isActive = true;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private VendorStatus status = VendorStatus.PENDING;

	@PrePersist
	public void prePersist() {
		joining_date = new Timestamp(System.currentTimeMillis());
	}
}
