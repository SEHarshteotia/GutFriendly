package com.gutfriendly.app.admin.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gutfriendly.app.admin.dto.response.InspectionResponse;
import com.gutfriendly.app.admin.dto.response.InspectorSummaryResponse;
import com.gutfriendly.app.admin.dto.response.ShopResponse;
import com.gutfriendly.app.admin.enums.InspectionStatus;
import com.gutfriendly.app.admin.enums.InspectorRecommendation;
import com.gutfriendly.app.admin.enums.ServiceAvailabilityStatus;
import com.gutfriendly.app.admin.enums.ShopStatus;
import com.gutfriendly.app.inspector.mapper.InspectionMapper;
import com.gutfriendly.app.inspector.model.InspectionDetails;
import com.gutfriendly.app.inspector.model.InspectorDetails;
import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.admin.repository.InspectionDetailsRepository;
import com.gutfriendly.app.admin.repository.InspectorDetailsRepo;
import com.gutfriendly.app.admin.repository.ShopDetailsRepository;
import com.gutfriendly.app.user.exception.ConflictException;
import com.gutfriendly.app.user.exception.ResourceNotFoundException;
import com.gutfriendly.app.user.service.GutTrustScoreService;

@Service
public class InspectionServiceImpl implements InspectionService {

	private final InspectionDetailsRepository inspectionRepo;

	private final InspectorDetailsRepo inspectorRepo;

	private final ShopDetailsRepository shopRepo;

	private final GutTrustScoreService gutTrustScoreService;

	InspectionServiceImpl(InspectionDetailsRepository inspectionRepo, InspectorDetailsRepo inspectorRepo,
			ShopDetailsRepository shopRepo, GutTrustScoreService gutTrustScoreService) {
		this.inspectionRepo = inspectionRepo;
		this.inspectorRepo = inspectorRepo;
		this.shopRepo = shopRepo;
		this.gutTrustScoreService = gutTrustScoreService;
	}

	@Override
	public Page<InspectionResponse> getAllInspections(int page, int size, String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);

		Page<InspectionDetails> inspectionPage = inspectionRepo.findAll(pageable);

		List<InspectionResponse> response = new ArrayList<>();

		for (InspectionDetails inspect : inspectionPage.getContent()) {
			response.add(InspectionMapper.toDto(inspect));

		}

		return new PageImpl<>(response, pageable, inspectionPage.getTotalElements());
	}

	@Override
	public InspectionResponse getInspectionById(int inspectionId) {
		Optional<InspectionDetails> optionalInspection = inspectionRepo.findById(inspectionId);
		if (optionalInspection.isEmpty()) {
			throw new ResourceNotFoundException("Inspection not found");
		}

		InspectionDetails inspection = optionalInspection.get();

		return InspectionMapper.toDto(inspection);
	}

	@Override
	public Page<InspectionResponse> getInspectionsByStatus(InspectionStatus status, int page, int size, String sortBy,
			String direction) {

		Sort sort = direction.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);

		Page<InspectionDetails> inspectionPage = inspectionRepo.findByStatus(status, pageable);

		List<InspectionResponse> response = new ArrayList<>();

		for (InspectionDetails inspect : inspectionPage.getContent()) {
			response.add(InspectionMapper.toDto(inspect));

		}

		return new PageImpl<>(response, pageable, inspectionPage.getTotalElements());

	}

	@Override
	public Page<InspectionResponse> getInspectionsByShop(int shopId, int page, int size, String sortBy,
			String direction) {

		Sort sort = direction.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);

		Page<InspectionDetails> inspectionPage = inspectionRepo.findByShop_ShopId(shopId, pageable);

		List<InspectionResponse> response = new ArrayList<>();

		for (InspectionDetails inspection : inspectionPage.getContent()) {
			response.add(InspectionMapper.toDto(inspection));
		}
		return new PageImpl<>(response, pageable, inspectionPage.getTotalElements());
	}

	@Override
	public Page<InspectionResponse> getInspectionsByInspector(int inspectorId, int page, int size, String sortBy,
			String direction) {
		Sort sort = direction.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);

		Page<InspectionDetails> inspectionPage = inspectionRepo.findByInspector_InspectorId(inspectorId, pageable);

		List<InspectionResponse> response = new ArrayList<>();

		for (InspectionDetails inspection : inspectionPage.getContent()) {
			response.add(InspectionMapper.toDto(inspection));
		}
		return new PageImpl<>(response, pageable, inspectionPage.getTotalElements());
	}

	@Override
	public List<InspectorSummaryResponse> getAllInspectors() {
		return inspectorRepo.findAll().stream()
				.map(inspector -> new InspectorSummaryResponse(
						inspector.getInspectorId(),
						inspector.getFirstName(),
						inspector.getLastName(),
						inspector.getEmail(),
						inspector.getPhoneNo(),
						inspector.getStatus().name()))
				.toList();
	}

	@Override
	public InspectionResponse assignInspector(int inspectionId, int inspectorId) {
		Optional<InspectionDetails> optionalInspection = inspectionRepo.findById(inspectionId);

		if (optionalInspection.isEmpty()) {
			throw new ResourceNotFoundException("Inspection not found");
		}

		// Find inspector
		Optional<InspectorDetails> optionalInspector = inspectorRepo.findByInspectorId(inspectorId);

		if (optionalInspector.isEmpty()) {
			throw new ResourceNotFoundException("Inspector not found");
		}

		// Get objects
		InspectionDetails inspection = optionalInspection.get();

		if (inspection.getStatus() != InspectionStatus.SCHEDULED) {
			throw new ConflictException("Only scheduled inspections can be assigned to an inspector.");
		}

		InspectorDetails inspector = optionalInspector.get();
		// Assign inspector
		inspection.setInspector(inspector);
		// Change inspection status
		inspection.setStatus(InspectionStatus.ASSIGNED);

		// Save
		InspectionDetails savedInspection = inspectionRepo.save(inspection);
		// Return DTO
		return InspectionMapper.toDto(savedInspection);

	}

	@Override
	public InspectionResponse reviewInspection(int inspectionId) {
		Optional<InspectionDetails> optionalInspection = inspectionRepo.findById(inspectionId);

		if (optionalInspection.isEmpty()) {
			throw new ResourceNotFoundException("Inspection not found");
		}

		// Get objects
		InspectionDetails inspection = optionalInspection.get();

		if (inspection.getStatus() != InspectionStatus.REPORT_SUBMITTED) {
			throw new ConflictException("Only submitted inspections can be reviewed.");
		}
		inspection.setStatus(InspectionStatus.UNDER_ADMIN_REVIEW);

		inspection.setReviewedByAdmin(true);
		inspection.setReviewedAt(LocalDateTime.now());

		// Save

		InspectionDetails savedInspection = inspectionRepo.save(inspection);

		ShopDetails shop = inspection.getShop();

		shop.setServiceAvailabilityStatus(ServiceAvailabilityStatus.SERVICEABLE);
		shop.setStatus(ShopStatus.PENDING);

		shopRepo.save(shop);

		// Return DTO
		return InspectionMapper.toDto(savedInspection);

	}

	@Override
	public InspectionResponse approveInspection(int inspectionId) {
		// Step 1 : Find Inspection
		Optional<InspectionDetails> optionalInspection = inspectionRepo.findById(inspectionId);

		if (optionalInspection.isEmpty()) {
			throw new ResourceNotFoundException("Inspection not found");
		}

		// Step 2 : Get Inspection
		InspectionDetails inspection = optionalInspection.get();

		// Step 3 : Validation
		if (inspection.getStatus() != InspectionStatus.UNDER_ADMIN_REVIEW) {
			throw new ConflictException("Only inspections under admin review can be approved.");
		}

		// Step 4 : Update Inspection
		inspection.setStatus(InspectionStatus.APPROVED);
		inspection.setCompletedAt(LocalDateTime.now());

		// Step 5 : Update Shop
		ShopDetails shop = inspection.getShop();

		shop.setStatus(ShopStatus.VERIFIED);
		shop.setBlocked(false);
		shop.setServiceAvailabilityStatus(ServiceAvailabilityStatus.SERVICEABLE);
		shop.setIsOpen(true);

		Double inspectionScore = inspection.getOverallInspectionScore();
		if (inspectionScore != null) {
			shop.setInspectionTrustScore(Math.round(inspectionScore * 100.0) / 100.0);
		}

		shop.setVerifiedAt(LocalDateTime.now());

		// Step 6 : Save Shop
		shopRepo.save(shop);
		gutTrustScoreService.recalculateFinalScore(shop.getShopId());

		// Step 7 : Save Inspection
		InspectionDetails savedInspection = inspectionRepo.save(inspection);

		// Step 8 : Return DTO
		return InspectionMapper.toDto(savedInspection);
	}

	@Override
	public InspectionResponse rejectInspection(int inspectionId, String rejectionReason) {
		// Step 1 : Find Inspection
		Optional<InspectionDetails> optionalInspection = inspectionRepo.findById(inspectionId);

		if (optionalInspection.isEmpty()) {
			throw new ResourceNotFoundException("Inspection not found");
		}

		// Step 2 : Get Inspection
		InspectionDetails inspection = optionalInspection.get();

		// Step 3 : Validation
		if (inspection.getStatus() != InspectionStatus.UNDER_ADMIN_REVIEW) {
			throw new ConflictException("Only inspections under admin review can be rejected.");
		}

		// Step 4 : Update Inspection
		inspection.setStatus(InspectionStatus.REJECTED);
		inspection.setCompletedAt(LocalDateTime.now());

		inspection.setAdminRemarks(rejectionReason);

		// Step 5 : Update Shop
		ShopDetails shop = inspection.getShop();

		shop.setStatus(ShopStatus.REJECTED);
		shop.setBlocked(false);
		shop.setServiceAvailabilityStatus(ServiceAvailabilityStatus.NOT_SERVICEABLE);

		// Step 6 : Save Shop
		shopRepo.save(shop);

		// Step 7 : Save Inspection
		InspectionDetails savedInspection = inspectionRepo.save(inspection);

		// Step 8 : Return DTO
		return InspectionMapper.toDto(savedInspection);

	}

	@Override
	public InspectionResponse sendForReInspection(int inspectionId, String reason) {

		// Step 1 : Find Existing Inspection
		InspectionDetails oldInspection = inspectionRepo.findById(inspectionId)
				.orElseThrow(() -> new ResourceNotFoundException("Inspection not found"));

		// Step 2 : Validation
		if (oldInspection.getStatus() != InspectionStatus.UNDER_ADMIN_REVIEW) {
			throw new ConflictException("Only inspections under admin review can be sent for re-inspection.");
		}

		// Step 3 : Close Old Inspection
		oldInspection.setStatus(InspectionStatus.CLOSED_FOR_REINSPECTION);
		oldInspection.setAdminRemarks(reason);
		oldInspection.setReviewedByAdmin(true);
		oldInspection.setReviewedAt(LocalDateTime.now());
		oldInspection.setCompletedAt(LocalDateTime.now());

		inspectionRepo.save(oldInspection);

		// Step 4 : Update Shop
		ShopDetails shop = oldInspection.getShop();

		shop.setStatus(ShopStatus.PENDING);
		shop.setBlocked(false);
		shop.setServiceAvailabilityStatus(ServiceAvailabilityStatus.NOT_SERVICEABLE);

		shopRepo.save(shop);

		// Step 5 : Create New Inspection
		InspectionDetails newInspection = new InspectionDetails();

		newInspection.setVendor(oldInspection.getVendor());
		newInspection.setShop(oldInspection.getShop());

		newInspection.setInspectionDate(LocalDateTime.now());

		newInspection.setStatus(InspectionStatus.SCHEDULED);

		newInspection.setOverallInspectionScore(0.0);

		newInspection.setRecommendation(InspectorRecommendation.PENDING);

		newInspection.setReviewedByAdmin(false);

		newInspection.setInspectorRemarks(null);
		newInspection.setAdminRemarks(null);
		newInspection.setCompletedAt(null);
		newInspection.setReviewedAt(null);

		InspectionDetails savedInspection = inspectionRepo.save(newInspection);

		// Step 6 : Return New Inspection
		return InspectionMapper.toDto(savedInspection);
	}
}
