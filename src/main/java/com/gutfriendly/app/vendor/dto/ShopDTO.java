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
public class ShopDTO {

	private Long shopId;
	private String shopName;
	private String imageUrl;
	private boolean open;
	private LocalTime openTime;
	private boolean onlineOrdersEnabled;
	private Integer estimatedPrepTimeMinutes;
	private VendorStatus status;
	private Double rating;
	private Long ratingCount;
	private ShopAddressDTO address;

	public static ShopDTO from(ShopDetails shop) {
		if (shop == null) {
			return null;
		}

		return new ShopDTO(
				(long) shop.getShopId(),
				shop.getShopName(),
				shop.getImageUrl(),
				Boolean.TRUE.equals(shop.getIsOpen()),
				shop.getOpenTime(),
				Boolean.TRUE.equals(shop.getOnlineOrdersEnabled()),
				shop.getEstimatedPrepTimeMinutes(),
				ShopStatusMapper.toVendorStatus(shop),
				shop.getRating(),
				shop.getRatingCount() != null ? shop.getRatingCount() : 0L,
				AddressMapper.toDto(shop.getAddress_id()));
	}
}
