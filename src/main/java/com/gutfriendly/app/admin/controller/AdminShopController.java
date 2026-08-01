package com.gutfriendly.app.admin.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.admin.dto.request.BlockShopRequest;
import com.gutfriendly.app.admin.dto.request.ReInspectionRequest;
import com.gutfriendly.app.admin.dto.request.RejectShopRequest;
import com.gutfriendly.app.admin.dto.response.ShopResponse;
import com.gutfriendly.app.admin.enums.ServiceAvailabilityStatus;
import com.gutfriendly.app.admin.enums.ShopStatus;
import com.gutfriendly.app.admin.service.ShopsService;

@RestController
@RequestMapping("/admin")
public class AdminShopController {
	
	private final ShopsService shopService;

	AdminShopController(ShopsService shopService) {
		this.shopService = shopService;
	}
	
	
	@GetMapping("/shops")
	public Page<ShopResponse>getAllShops(
			@RequestParam (defaultValue = "0") int page ,   
			@RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction ) {
		
		  return shopService.getAllShops(
	                page,
	                size,
	                sortBy,
	                direction
	        );}
	
	
	@GetMapping("/shops/status/{status}")
    public Page<ShopResponse> getShopsByStatus(

            @PathVariable ShopStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {
        return shopService.getShopsByStatus(status,page,size,sortBy,direction);
    }
	
	@GetMapping("/shops/availability-Status/{status}")
	public Page<ShopResponse> getShopsByServiceAvailabilityStatus( 
			@PathVariable ServiceAvailabilityStatus status ,  
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction){
		return shopService.getShopsByServiceAvailabilityStatus(status, page, size, sortBy, direction);
	}
	
	@GetMapping("/shops/search")
	public Page<ShopResponse> getShopsByShopName(
			@RequestParam String shopName ,  
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction){
		return shopService.getShopsByShopName(shopName, page, size, sortBy, direction);
	}
	
	@GetMapping("/shops/{shopId}")
	public ShopResponse getShopById(@PathVariable int shopId ) {
		return shopService.getShopById(shopId);
	}
	 
	@PatchMapping("/shops/{shopId}/block")
	public ShopResponse blockShop(
	        @PathVariable int shopId,  @RequestBody BlockShopRequest request) {

	    return shopService.blockShop(shopId,request.getBlockShopRequestReason());
	}
	
	@PatchMapping("/shops/{shopId}/unblock")
	public ShopResponse UnblockShop(
	        @PathVariable int shopId) {

	    return shopService.UnblockShop(shopId);
	}
	
	
	
	
	
	
	
	

}
