package com.gutfriendly.app.vendor.dto;

import java.time.LocalTime;

import com.gutfriendly.app.vendor.model.Store;
import com.gutfriendly.app.vendor.status.VendorStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Full store details including hours, operational flags, and address.
 * <p>
 * Used by {@code GET/PUT /vendor/{vendorId}/shops/{shopId}/store}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreDetailsDTO {

	private Long shopId;
	private String storeName;
	private String imageUrl;
	private boolean open;
	private LocalTime openTime;
	private LocalTime closeTime;
	private boolean onlineOrdersEnabled;
	private Integer estimatedPrepTimeMinutes;
	private VendorStatus status;
	private Double rating;
	private Long ratingCount;
	private StoreAddressDTO address;

	public static StoreDetailsDTO from(Store store) {
		return new StoreDetailsDTO(
				store.getStoreId(),
				store.getStoreName(),
				store.getImageUrl(),
				Boolean.TRUE.equals(store.getIsOpen()),
				store.getOpenTime(),
				store.getCloseTime(),
				Boolean.TRUE.equals(store.getOnlineOrdersEnabled()),
				store.getEstimatedPrepTimeMinutes(),
				store.getStatus(),
				store.getRating(),
				store.getRatingCount(),
				StoreAddressDTO.from(store.getAddress()));
	}
}
