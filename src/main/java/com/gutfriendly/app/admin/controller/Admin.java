package com.gutfriendly.app.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.admin.dto.request.AdminRegisterRequest;
import com.gutfriendly.app.admin.dto.response.ResponseDto;
import com.gutfriendly.app.admin.model.AdminDetails;
import com.gutfriendly.app.admin.service.AdminService;

@RestController
@RequestMapping("/admin")
public class Admin {

	final AdminService as;

	Admin(AdminService as) {
		this.as = as;
	}

	@PostMapping("/registration")
	public ResponseEntity<?> adminRegistration(@RequestBody AdminRegisterRequest request) {
		as.saveAdmin(request);
		ResponseDto response = new ResponseDto("Registered successfully", "Success");
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<?> adminLogin(@RequestBody AdminDetails ad) {
		ResponseDto response = new ResponseDto("Login successfully", "Success");
		return ResponseEntity.ok(response);
	}
}
