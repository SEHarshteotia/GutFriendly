package com.gutfriendly.app.vendor.dto;

import java.time.LocalTime;

import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.vendor.mapper.AddressMapper;
import com.gutfriendly.app.vendor.mapper.ShopStatusMapper;
import com.gutfriendly.app.vendor.enums.VendorStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopDetailsDTO {

	private Long shopId;
	private String shopName;
	private String imageUrl;
	private boolean open;
	private LocalTime openTime;
	private LocalTime closeTime;
	private boolean onlineOrdersEnabled;
	private Integer estimatedPrepTimeMinutes;
	private VendorStatus status;
	private Double rating;
	private Long ratingCount;
	private ShopAddressDTO address;

	public static ShopDetailsDTO from(ShopDetails shop) {
		return new ShopDetailsDTO(
				(long) shop.getShopId(),
				shop.getShopName(),
				shop.getImageUrl(),
				Boolean.TRUE.equals(shop.getIsOpen()),
				shop.getOpenTime(),
				shop.getCloseTime(),
				Boolean.TRUE.equals(shop.getOnlineOrdersEnabled()),
				shop.getEstimatedPrepTimeMinutes(),
				ShopStatusMapper.toVendorStatus(shop),
				shop.getRating(),
				shop.getRatingCount() != null ? shop.getRatingCount() : 0L,
				AddressMapper.toDto(shop.getAddress_id()));
	}
}
