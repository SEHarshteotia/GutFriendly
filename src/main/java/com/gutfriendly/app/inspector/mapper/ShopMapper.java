package com.gutfriendly.app.inspector.mapper;

import com.gutfriendly.app.admin.dto.response.ShopResponse;
import com.gutfriendly.app.admin.model.ShopDetails;

public class ShopMapper {
	
	public static ShopResponse toDto(ShopDetails shop) {
		   ShopResponse dto = new ShopResponse();

	        dto.setShopId(shop.getShopId());
	        dto.setShopName(shop.getShopName());
	        dto.setGstNo(shop.getGstNo());
	        dto.setCategory(shop.getCategory());
	        dto.setStatus(shop.getStatus());
	        dto.setServiceAvailabilityStatus(shop.getServiceAvailabilityStatus());
	        dto.setBlocked(shop.getBlocked());
	        dto.setFinalGutTrustScore(shop.getFinalGutTrustScore());
	        dto.setCreatedAt(shop.getCreatedAt());
	        dto.setAdminRemarks(shop.getAdminRemarks());
	       

	        return dto;
	}
}
