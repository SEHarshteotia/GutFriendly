package com.gutfriendly.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.dto.VendorLocationRequestDTO;
import com.gutfriendly.app.dto.VendorLocationResponseDTO;
import com.gutfriendly.app.service.VendorLocationService;

@RestController
@RequestMapping("/vendor")
public class VendorLocationController {

	private final VendorLocationService service;

	VendorLocationController(VendorLocationService service) {
		this.service = service;
	}

	@PostMapping("/location")
	public ResponseEntity<VendorLocationResponseDTO> savelocation(@RequestBody VendorLocationRequestDTO request) {
		return ResponseEntity.ok(service.saveLocation(request));
	}

}
