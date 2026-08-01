package com.gutfriendly.app.admin.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.stereotype.Service;

import com.gutfriendly.app.admin.dto.request.AdminRegisterRequest;
import com.gutfriendly.app.admin.model.AdminDetails;
import com.gutfriendly.app.admin.repository.AdminDetailsRepository;

@Service("firstService")
public class AdminServiceImpl implements AdminService {

	final AdminDetailsRepository ad;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(BCryptVersion.$2A, 12);

	AdminServiceImpl(AdminDetailsRepository ad) {
		this.ad = ad;
	}

	@Override
	public AdminDetails saveAdmin(AdminRegisterRequest request) {

		if (request.getEmail().isEmpty()) {
			throw new RuntimeException("Email cannot be Empty");
		}

		if (ad.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email already Exisist ");
		}

		if (request.getPassword().length() < 8) {
			throw new RuntimeException("Minimum Password Length is 8 ");
		}

		if (!request.getPassword().matches("^(?=.*[A-Z])(?=.*[0-9]).{8,}$")) {
			throw new RuntimeException("Password must contain uppercase and number");
		}

		if (request.getFirstName() == null) {
			throw new RuntimeException("First Name  required");
		}
		if (request.getLastName() == null) {
			throw new RuntimeException("Last Name  required");
		}

		if (!request.getPhoneNo().matches("^[6-9][0-9]{9}$")) {

			throw new RuntimeException("Invalid mobile number");
		}

		if (!request.getPassword().equals(request.getConfirmPassword())) {
			throw new RuntimeException("Password and Confirm Password do not match");
		}

		AdminDetails admin = new AdminDetails();
		admin.setFirstName(request.getFirstName());
		admin.setLastName(request.getLastName());
		admin.setEmail(request.getEmail());
		admin.setPhoneNo(request.getPhoneNo());
		admin.setPassword(encoder.encode(request.getPassword()));

		return ad.save(admin);

	}

}
