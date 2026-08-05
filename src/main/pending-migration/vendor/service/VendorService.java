package com.gutfriendly.app.vendor.service;



import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;



import com.gutfriendly.app.vendor.dto.VendorLoginDTO;

import com.gutfriendly.app.vendor.dto.VendorLoginResponseDTO;

import com.gutfriendly.app.vendor.dto.VendorProfileDTO;

import com.gutfriendly.app.vendor.dto.VendorRegisterRequestDTO;

import com.gutfriendly.app.vendor.dto.VendorRegisterResponseDTO;

import com.gutfriendly.app.vendor.model.VendorDetails;

import com.gutfriendly.app.vendor.repository.VendorRepo;
import com.gutfriendly.app.vendor.util.PhoneNumberUtil;



/**

 * Handles vendor registration and authentication.

 */

@Service

public class VendorService {



	private final VendorRepo repo;

	private final StoreService shopService;



	VendorService(VendorRepo repo, StoreService shopService) {

		this.repo = repo;

		this.shopService = shopService;

	}



	/**

	 * Creates a new vendor account from the registration payload.

	 *

	 * @param request registration fields; {@code fName}, {@code lName}, {@code phoneNo},

	 *                and {@code password} are required

	 * @return the new vendor ID and success message

	 * @throws ResponseStatusException with {@link HttpStatus#BAD_REQUEST} if required fields are missing

	 */

	public VendorRegisterResponseDTO register(VendorRegisterRequestDTO request) {

		validateRegistration(request);



		VendorDetails vendor = new VendorDetails();

		vendor.setFName(request.getFName().trim());

		vendor.setMName(isBlank(request.getMName()) ? null : request.getMName().trim());

		vendor.setLName(request.getLName().trim());

		vendor.setPhoneNo(PhoneNumberUtil.normalize(request.getPhoneNo()));

		vendor.setPassword(request.getPassword());

		vendor.setEmail(isBlank(request.getEmail()) ? null : request.getEmail().trim());

		vendor.setAadharNo(isBlank(request.getAadharNo()) ? null : request.getAadharNo().trim());

		vendor.setPanNo(isBlank(request.getPanNo()) ? null : request.getPanNo().trim());



		VendorDetails saved = repo.save(vendor);

		return new VendorRegisterResponseDTO(saved.getVendor_id(), "Vendor registered successfully");

	}



	/**

	 * Authenticates a vendor by phone number and password.

	 *

	 * @param loginDTO credentials containing phone number and password

	 * @return login message, vendor profile, and list of shops

	 * @throws ResponseStatusException with {@link HttpStatus#UNAUTHORIZED} if credentials are invalid

	 */

	public VendorLoginResponseDTO login(VendorLoginDTO loginDTO) {

		String phoneNo = PhoneNumberUtil.normalize(loginDTO.getPhoneNo());

		VendorDetails vendor = repo.findByPhoneNoAndPassword(phoneNo, loginDTO.getPassword());

		if (vendor == null) {

			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");

		}



		return new VendorLoginResponseDTO(

				"Login successful",

				VendorProfileDTO.from(vendor),

				shopService.listShops(vendor.getVendor_id()));

	}



	private void validateRegistration(VendorRegisterRequestDTO request) {

		if (isBlank(request.getFName()) || isBlank(request.getLName())) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First and last name are required");

		}

		if (isBlank(request.getPhoneNo())) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number is required");

		}

		if (isBlank(request.getPassword())) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");

		}

	}



	private boolean isBlank(String value) {

		return value == null || value.isBlank();

	}

}

