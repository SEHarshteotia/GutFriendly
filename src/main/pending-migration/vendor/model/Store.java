package com.gutfriendly.app.vendor.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A vendor's shop or restaurant outlet with operational settings and onboarding status.
 * <p>
 * Owned by one {@link VendorDetails}, has optional {@link StoreAddress},
 * and aggregates menu items, orders, and reviews.
 */
@Entity
@Table(name = "store")
@Data
@NoArgsConstructor
public class Store {//change to shop details

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long storeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id", nullable = false)
	private VendorDetails vendor;

	@Column(nullable = false, length = 100)
	private String storeName;
	//add gst number
	@Column(length = 255)
	private String imageUrl;

	private Boolean isOpen = false;

	private Boolean onlineOrdersEnabled = true;

	private LocalTime openTime = LocalTime.of(9, 0);

	private LocalTime closeTime = LocalTime.of(22, 0);

	private Integer estimatedPrepTimeMinutes = 15;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private VendorStatus status = VendorStatus.PENDING;

	/** Cached average rating for the shop; populated by the rating sync API. */
	private Double rating;

	/** Number of ratings used to compute {@link #rating}. */
	private Long ratingCount = 0L;

	@OneToOne(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private StoreAddress address;

	//create separate table
	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<MenuItem> menuItems = new ArrayList<>();

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<StoreOrder> orders = new ArrayList<>();

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<StoreReview> reviews = new ArrayList<>();
}
