package com.gutfriendly.app.admin.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.admin.dto.request.AdminLoginRequest;
import com.gutfriendly.app.admin.dto.request.AdminRegisterRequest;
import com.gutfriendly.app.admin.dto.response.ResponseDto;
import com.gutfriendly.app.admin.model.AdminDetails;
import com.gutfriendly.app.admin.service.AdminAuthService;
import com.gutfriendly.app.admin.service.AdminService;
import com.gutfriendly.app.security.AuthRole;
import com.gutfriendly.app.security.AuthTokenService;

@RestController
@RequestMapping("/admin")
public class Admin {

	final AdminService as;

	final AdminAuthService adminAuthService;

	final AuthTokenService authTokenService;

	Admin(AdminService as, AdminAuthService adminAuthService, AuthTokenService authTokenService) {
		this.as = as;
		this.adminAuthService = adminAuthService;
		this.authTokenService = authTokenService;
	}

	@PostMapping("/registration")
	public ResponseEntity<?> adminRegistration(@RequestBody AdminRegisterRequest request) {
		as.saveAdmin(request);
		ResponseDto response = new ResponseDto("Registered successfully", "Success");
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<?> adminLogin(@RequestBody AdminLoginRequest request) {

		AdminDetails admin = adminAuthService.login(request);

		return ResponseEntity.ok(
				Map.of(
						"message", "Login successfully",
						"token", authTokenService.issue(AuthRole.ADMIN, admin.getId()),
						"status", "Success",
						"adminId", admin.getId(),
						"firstName", admin.getFirstName(),
						"lastName", admin.getLastName(),
						"email", admin.getEmail()));
	}
}
