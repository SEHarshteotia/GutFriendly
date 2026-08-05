package com.gutfriendly.app.inspector.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.inspector.dto.InspectorLoginDTO;
import com.gutfriendly.app.inspector.model.InspectorDetails;
import com.gutfriendly.app.inspector.service.InspectorAuthService;

@RestController
@RequestMapping("/inspector")
public class InspectorAuthController {

	private final InspectorAuthService authService;

	InspectorAuthController(InspectorAuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody InspectorLoginDTO loginDTO) {
		InspectorDetails inspector = authService.login(loginDTO);

		return ResponseEntity.ok(
				Map.of(
						"message", "Login Successful",
						"inspectorId", inspector.getInspectorId(),
						"firstName", inspector.getFirstName(),
						"lastName", inspector.getLastName()));
	}
}
