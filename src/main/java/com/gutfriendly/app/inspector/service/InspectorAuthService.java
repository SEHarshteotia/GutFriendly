package com.gutfriendly.app.inspector.service;

import org.springframework.stereotype.Service;

import com.gutfriendly.app.admin.enums.InspectorStatus;
import com.gutfriendly.app.admin.repository.InspectorDetailsRepo;
import com.gutfriendly.app.inspector.dto.InspectorLoginDTO;
import com.gutfriendly.app.inspector.model.InspectorDetails;
import com.gutfriendly.app.user.exception.BadRequestException;
import com.gutfriendly.app.user.exception.ConflictException;
import com.gutfriendly.app.user.exception.ResourceNotFoundException;

@Service
public class InspectorAuthService {

	private final InspectorDetailsRepo inspectorRepo;

	InspectorAuthService(InspectorDetailsRepo inspectorRepo) {
		this.inspectorRepo = inspectorRepo;
	}

	public InspectorDetails login(InspectorLoginDTO loginDTO) {
		if (loginDTO == null) {
			throw new BadRequestException("Login details are required");
		}

		if (loginDTO.getEmail() == null || loginDTO.getEmail().trim().isEmpty()) {
			throw new BadRequestException("Email is required");
		}

		if (loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
			throw new BadRequestException("Password is required");
		}

		InspectorDetails inspector = inspectorRepo
				.findByEmail(loginDTO.getEmail().trim())
				.orElseThrow(() -> new ResourceNotFoundException("Inspector not found"));

		if (!inspector.getPassword().equals(loginDTO.getPassword())) {
			throw new BadRequestException("Invalid email or password");
		}

		if (inspector.getStatus() != InspectorStatus.ACTIVE) {
			throw new ConflictException("Inspector account is inactive");
		}

		return inspector;
	}
}
