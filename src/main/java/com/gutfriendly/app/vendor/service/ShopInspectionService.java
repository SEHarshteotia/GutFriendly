package com.gutfriendly.app.vendor.service;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.gutfriendly.app.admin.dto.response.InspectionResponse;
import com.gutfriendly.app.admin.enums.InspectionStatus;
import com.gutfriendly.app.admin.enums.ServiceAvailabilityStatus;
import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.admin.model.VendorDetails;
import com.gutfriendly.app.admin.repository.InspectionDetailsRepository;
import com.gutfriendly.app.inspector.mapper.InspectionMapper;
import com.gutfriendly.app.inspector.model.InspectionDetails;
import com.gutfriendly.app.vendor.dto.BookInspectionRequestDTO;

@Service
public class ShopInspectionService {

	private final VendorContextService contextService;
	private final InspectionDetailsRepository inspectionRepository;

	ShopInspectionService(VendorContextService contextService, InspectionDetailsRepository inspectionRepository) {
		this.contextService = contextService;
		this.inspectionRepository = inspectionRepository;
	}

	@Transactional
	public InspectionResponse bookInspection(Integer vendorId, Long shopId, BookInspectionRequestDTO request) {
		if (request == null || request.getInspectionDate() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Inspection date and time are required");
		}

		if (request.getInspectionDate().isBefore(LocalDateTime.now())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Inspection date must be in the future");
		}

		ShopDetails shop = contextService.findShop(vendorId, shopId);
		VendorDetails vendor = contextService.findVendor(vendorId);

		if (shop.getAddress_id() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Shop location must be saved before booking an inspection");
		}

		if (shop.getServiceAvailabilityStatus() != ServiceAvailabilityStatus.SERVICEABLE) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Inspection can only be booked for a shop in a serviceable pincode area");
		}

		if (inspectionRepository.existsByShop_ShopIdAndStatusIn(shop.getShopId(),
				InspectionStatus.activeInspectionStatuses())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"This shop already has an active inspection in progress");
		}

		InspectionDetails inspection = new InspectionDetails();
		inspection.setVendor(vendor);
		inspection.setShop(shop);
		inspection.setInspectionDate(request.getInspectionDate());
		inspection.setStatus(InspectionStatus.SCHEDULED);
		inspection.setInspector(null);

		return InspectionMapper.toDto(inspectionRepository.save(inspection));
	}
}
