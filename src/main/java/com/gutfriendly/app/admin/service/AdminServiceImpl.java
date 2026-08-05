package com.gutfriendly.app.admin.service;

import org.springframework.stereotype.Service;

import com.gutfriendly.app.admin.dto.request.AdminRegisterRequest;
import com.gutfriendly.app.admin.model.AdminDetails;
import com.gutfriendly.app.admin.repository.AdminDetailsRepository;
import com.gutfriendly.app.user.exception.BadRequestException;
import com.gutfriendly.app.user.exception.ConflictException;

@Service("firstService")
public class AdminServiceImpl implements AdminService {

	final AdminDetailsRepository ad;

	AdminServiceImpl(AdminDetailsRepository ad) {
		this.ad = ad;
	}

	@Override
	public AdminDetails saveAdmin(AdminRegisterRequest request) {

		if (request.getEmail().isEmpty()) {
			throw new BadRequestException("Email cannot be Empty");
		}

		if (ad.existsByEmail(request.getEmail())) {
			throw new ConflictException("Email already Exisist ");
		}

		if (request.getPassword().length() < 8) {
			throw new BadRequestException("Minimum Password Length is 8 ");
		}

		if (!request.getPassword().matches("^(?=.*[A-Z])(?=.*[0-9]).{8,}$")) {
			throw new BadRequestException("Password must contain uppercase and number");
		}

		if (request.getFirstName() == null) {
			throw new BadRequestException("First Name  required");
		}
		if (request.getLastName() == null) {
			throw new BadRequestException("Last Name  required");
		}

		if (!request.getPhoneNo().matches("^[6-9][0-9]{9}$")) {
			throw new BadRequestException("Invalid mobile number");
		}

		if (!request.getPassword().equals(request.getConfirmPassword())) {
			throw new BadRequestException("Password and Confirm Password do not match");
		}

		AdminDetails admin = new AdminDetails();
		admin.setFirstName(request.getFirstName());
		admin.setLastName(request.getLastName());
		admin.setEmail(request.getEmail());
		admin.setPhoneNo(request.getPhoneNo());
		admin.setPassword(request.getPassword());

		return ad.save(admin);

	}

}
