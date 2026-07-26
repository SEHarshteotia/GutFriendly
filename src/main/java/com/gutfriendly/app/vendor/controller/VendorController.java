package com.gutfriendly.app.vendor.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.vendor.dto.VendorLoginDTO;
import com.gutfriendly.app.vendor.model.VendorDetails;
import com.gutfriendly.app.vendor.service.VendorService;

@RestController
@RequestMapping("/vendor")
@CrossOrigin(origins = "http://localhost:5173")
public class VendorController {

	private final VendorService service;

	VendorController(VendorService service) {
		this.service = service;
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody VendorDetails vendor) {

		System.out.println(vendor);
		service.saveVendor(vendor);
		return ResponseEntity.ok("Vendor Registered");

	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody VendorLoginDTO loginDTO) {

		VendorDetails vendor = service.login(loginDTO.getPhoneNo(), loginDTO.getPassword());
		if (vendor == null) {
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("message", "Invalid Credentials"));
		}

		return ResponseEntity.ok(Map.of(
				"message", "Login Successful",
				"vendor", vendor));

	}

}
