package com.gutfriendly.app.admin.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.gutfriendly.app.admin.dto.response.ShopResponse;
import com.gutfriendly.app.admin.enums.ServiceAvailabilityStatus;
import com.gutfriendly.app.admin.enums.ShopStatus;
import com.gutfriendly.app.admin.model.ShopDetails;

public interface ShopsService {
	
	Page<ShopResponse> getAllShops(
	        int page,
	        int size,
	        String sortBy,
	        String direction);
	
	Page<ShopResponse> getShopsByStatus(
	        ShopStatus status,
	        int page,
	        int size,
	        String sortBy,
	        String direction);
	
	public Page<ShopResponse> getShopsByServiceAvailabilityStatus( 
			ServiceAvailabilityStatus status ,  
			int page, 
			int size, 
			String sortBy,
			String direction);
	
	public Page<ShopResponse> getShopsByShopName(
			String shopName,
			int page, 
			int size, 
			String sortBy,
			String direction);
	
	public ShopResponse getShopById(int shopId);
	

	
	public ShopResponse blockShop(int shopId , String BlockShopReason);
	
	public ShopResponse UnblockShop(int shopId);

}
