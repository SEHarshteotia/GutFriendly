package com.gutfriendly.app.vendor.dto;

import java.time.LocalTime;

import com.gutfriendly.app.vendor.model.Store;
import com.gutfriendly.app.vendor.status.VendorStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Shop summary returned by shop list, get, create, and update endpoints.
 * <p>
 * Used by shop endpoints under {@code /vendor/{vendorId}/shops} and login response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreDTO {

	private Long shopId;
	private String storeName;
	private String imageUrl;
	private boolean open;
	private LocalTime openTime;
	private boolean onlineOrdersEnabled;
	private Integer estimatedPrepTimeMinutes;
	private VendorStatus status;
	private Double rating;
	private Long ratingCount;
	private StoreAddressDTO address;

	public static StoreDTO from(Store store) {
		if (store == null) {
			return null;
		}

		return new StoreDTO(
				store.getStoreId(),
				store.getStoreName(),
				store.getImageUrl(),
				Boolean.TRUE.equals(store.getIsOpen()),
				store.getOpenTime(),
				Boolean.TRUE.equals(store.getOnlineOrdersEnabled()),
				store.getEstimatedPrepTimeMinutes(),
				store.getStatus(),
				store.getRating(),
				store.getRatingCount(),
				StoreAddressDTO.from(store.getAddress()));
	}
}
