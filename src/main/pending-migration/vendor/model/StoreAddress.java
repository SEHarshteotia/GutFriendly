package com.gutfriendly.app.vendor.model;

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

/**
 * 
 * Physical address for a vendor shop.
 * 
 * <p>
 * 
 * One-to-one with {@link Store}; used for serviceability checks via pincode.
 * 
 */

@Entity

@Table(name = "store_address")

@Data

@NoArgsConstructor

public class StoreAddress {

	@Id

	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long addressId;

	@OneToOne(fetch = FetchType.LAZY)

	@JoinColumn(name = "store_id", nullable = false, unique = true)

	private Store store;

	@Column(nullable = false, length = 100)

	private String houseNo;

	@Column(nullable = false, length = 150)

	private String street;

	@Column(nullable = false, length = 100)

	private String city;

	@Column(nullable = false, length = 100)

	private String state;

	@Column(nullable = false, length = 10)

	private String pincode;

	@Column(nullable = false)

	private String country = "India";

}
