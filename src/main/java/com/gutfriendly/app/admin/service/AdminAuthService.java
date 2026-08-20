package com.gutfriendly.app.admin.service;

import org.springframework.stereotype.Service;

import com.gutfriendly.app.admin.dto.request.AdminLoginRequest;
import com.gutfriendly.app.admin.model.AdminDetails;
import com.gutfriendly.app.admin.repository.AdminDetailsRepository;
import com.gutfriendly.app.common.security.PasswordHasher;
import com.gutfriendly.app.user.exception.BadRequestException;
import com.gutfriendly.app.user.exception.ConflictException;

/**
 * Credential check for {@code POST /admin/login}.
 *
 * <p>Deliberately mirrors {@code InspectorAuthService} so both staff logins
 * behave the same way. A missing account and a wrong password both raise the
 * same message, so the endpoint does not reveal which emails are registered.
 */
@Service
public class AdminAuthService {

	private final AdminDetailsRepository adminDetailsRepository;

	AdminAuthService(AdminDetailsRepository adminDetailsRepository) {
		this.adminDetailsRepository = adminDetailsRepository;
	}

	public AdminDetails login(AdminLoginRequest request) {

		if (request == null) {
			throw new BadRequestException("Login details are required");
		}

		if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
			throw new BadRequestException("Email is required");
		}

		if (request.getPassword() == null || request.getPassword().isEmpty()) {
			throw new BadRequestException("Password is required");
		}

		AdminDetails admin = adminDetailsRepository
				.findByEmail(request.getEmail().trim())
				.orElseThrow(() -> new BadRequestException("Invalid email or password"));

		if (!PasswordHasher.matches(request.getPassword(), admin.getPassword())) {
			throw new BadRequestException("Invalid email or password");
		}

		if (PasswordHasher.needsRehash(admin.getPassword())) {
			admin.setPassword(PasswordHasher.hash(request.getPassword()));
			adminDetailsRepository.save(admin);
		}

		if (!admin.isActiveStatus()) {
			throw new ConflictException("Admin account is inactive");
		}

		return admin;
	}
}
