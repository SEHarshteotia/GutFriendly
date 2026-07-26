package com.gutfriendly.app.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorStoreStatusDTO {

	private String storeName;
	private String imageUrl;
	private boolean open;
	private LocalTime openTime;
	private boolean onlineOrdersEnabled;
	private boolean deliveryPartnersEnabled;
	private Integer estimatedPrepTimeMinutes;
}
