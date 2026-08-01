package com.gutfriendly.app.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

	final AuthenticationManager authenticationManager;

	Admin(AuthenticationManager authenticationManager, AdminService as) {
		this.authenticationManager = authenticationManager;
		this.as = as;
	}

	@PostMapping("/registration")
	public ResponseEntity<?> adminRegistration(@RequestBody AdminRegisterRequest request) {
		System.out.print("Data recived");
		System.out.print(request);
		AdminDetails admin = as.saveAdmin(request);
		ResponseDto response = new ResponseDto("Registered successfully", "Success");
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<?> adminLogin(@RequestBody AdminDetails ad) {
		// try {
//		Authentication authentication = authenticationManager
//				.authenticate(new UsernamePasswordAuthenticationToken(ad.getEmail(), ad.getPassword()));
//		ResponseDto response = new ResponseDto("Login successfully", "Success");
//		 return ResponseEntity.ok(response);
//		} catch(Exception e) {
//
//	        return ResponseEntity
//	        .status(401)
//	        .body("Invalid email or password");
//	    }

		ResponseDto response = new ResponseDto("Login successfully", "Success");
		return ResponseEntity.ok(response);
	}
}

//@GetMapping("/token")
//public CsrfToken getToken(HttpServletRequest request) {
//	return (CsrfToken) request.getAttribute("_csrf");
//	
//}
