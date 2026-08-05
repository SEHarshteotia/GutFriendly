package com.gutfriendly.app.user.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.user.dto.ShopCardDTO;
import com.gutfriendly.app.user.dto.ShopDetailsDTO;
import com.gutfriendly.app.admin.enums.Category;
import com.gutfriendly.app.user.service.ShopService;


@RestController
@RequestMapping("/shops")
public class ShopController {
	
	
	@Autowired
	private ShopService service;
	
	@GetMapping
	public ResponseEntity<List<ShopCardDTO>> getAllShops(){
		return ResponseEntity.ok(service.getAllShops());
	}
	
	
	@GetMapping("/trusted-vendors")
	public ResponseEntity<List<ShopCardDTO>> getTrustedVendors(){
		return ResponseEntity.ok(service.getTrustedVendors());
	}
	
	
	@GetMapping("/search")
	public ResponseEntity<List<ShopCardDTO>> searchShops(@RequestParam String keyword){
		return ResponseEntity.ok(service.searchShops(keyword));
	}
	
	@GetMapping("/category/{category}")
	public ResponseEntity<List<ShopCardDTO>> getShopsByCategory(@PathVariable Category category){
		return ResponseEntity.ok(service.getShopsByCategory(category));
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<ShopDetailsDTO> getShopById(@PathVariable int id){
		return ResponseEntity.ok(service.getById(id));
	}
	
}
