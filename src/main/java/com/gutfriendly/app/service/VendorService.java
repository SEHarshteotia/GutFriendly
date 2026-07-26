package com.gutfriendly.app.service;

import org.springframework.stereotype.Service;

import com.gutfriendly.app.model.VendorDetails;
import com.gutfriendly.app.repository.VendorRepo;

@Service
public class VendorService {

	final VendorRepo repo;

	VendorService(VendorRepo repo) {
		this.repo = repo;
	}

	public void saveVendor(VendorDetails vendor) {

	    if (isBlank(vendor.getEmail())) {
	        vendor.setEmail(null);
	    }

	    if (isBlank(vendor.getAadharNo())) {
	        vendor.setAadharNo(null);
	    }

	    if (isBlank(vendor.getPanNo())) {
	        vendor.setPanNo(null);
	    }

	    repo.save(vendor);
	}

	public VendorDetails login(String phoneNo, String password) {

		return repo.findByPhoneNoAndPassword(phoneNo, password);
	}

	private boolean isBlank(String value) {
		return value == null || value.isBlank();
	}

}
