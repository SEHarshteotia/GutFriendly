package com.gutfriendly.app.model;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vendor_store")
@Data
@NoArgsConstructor
public class VendorStore {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long storeId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id", nullable = false, unique = true)
	private VendorDetails vendor;

	@Column(nullable = false, length = 100)
	private String storeName;

	@Column(length = 255)
	private String imageUrl;

	private Boolean isOpen = false;

	private Boolean onlineOrdersEnabled = true;

	private Boolean deliveryPartnersEnabled = true;

	private LocalTime openTime = LocalTime.of(9, 0);

	private Integer estimatedPrepTimeMinutes = 15;
}
