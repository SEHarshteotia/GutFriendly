package com.gutfriendly.app.inspector.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.gutfriendly.app.admin.dto.request.InspectionTestResultRequest;
import com.gutfriendly.app.admin.dto.response.InspectionResponse;
import com.gutfriendly.app.admin.dto.response.InspectionTestResultResponse;
import com.gutfriendly.app.admin.dto.response.TestCatalogResponse;
import com.gutfriendly.app.admin.enums.InspectionStatus;
import com.gutfriendly.app.admin.enums.InspectorRecommendation;
import com.gutfriendly.app.inspector.mapper.InspectionMapper;
import com.gutfriendly.app.inspector.mapper.InspectionTestResultMapper;
import com.gutfriendly.app.inspector.mapper.TestCatalogMapper;
import com.gutfriendly.app.inspector.model.InspectionDetails;
import com.gutfriendly.app.inspector.model.InspectionTestResult;
import com.gutfriendly.app.inspector.model.TestCatalog;
import com.gutfriendly.app.admin.repository.InspectionDetailsRepository;
import com.gutfriendly.app.admin.repository.InspectionTestResultRepo;
import com.gutfriendly.app.admin.repository.TestCatalogRepo;

public class InspectorInspectionServiceImpl implements InspectorInspectionService {

	private final InspectionDetailsRepository inspectionRepo;

	private final InspectionTestResultRepo resultRepo;

	private final TestCatalogRepo testRepo;

	InspectorInspectionServiceImpl(InspectionDetailsRepository inspectionRepo, InspectionTestResultRepo resultRepo,
			TestCatalogRepo testRepo) {
		this.inspectionRepo = inspectionRepo;
		this.resultRepo = resultRepo;
		this.testRepo = testRepo;
	}

	@Override
	public Page<InspectionResponse> getAssignedInspections(int inspectorId, int page, int size, String sortBy,
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
	public InspectionResponse getInspectionById(int inspectionId) {

		Optional<InspectionDetails> optionalInspection = inspectionRepo.findById(inspectionId);

		if (optionalInspection.isEmpty()) {
			throw new RuntimeException("Inspection not found.");
		}

		InspectionDetails inspection = optionalInspection.get();

		return InspectionMapper.toDto(inspection);

	}

	@Override
	public InspectionResponse startInspection(int inspectionId) {
		Optional<InspectionDetails> optionalInspection = inspectionRepo.findById(inspectionId);

		if (optionalInspection.isEmpty()) {
			throw new RuntimeException("Inspection Not Found");
		}
		InspectionDetails inspection = optionalInspection.get();

		if (inspection.getStatus() != InspectionStatus.ASSIGNED) {
			throw new RuntimeException("Only assigned inspections can be started.");
		}

		inspection.setStatus(InspectionStatus.IN_PROGRESS);
		InspectionDetails savedInspection = inspectionRepo.save(inspection);

		return InspectionMapper.toDto(savedInspection);
	}

	@Override
	public List<InspectionTestResultResponse> getInspectionTestResults(int inspectionId) {
		// Step 1 : Find Inspection
		Optional<InspectionDetails> optionalInspection = inspectionRepo.findById(inspectionId);

		if (optionalInspection.isEmpty()) {
			throw new RuntimeException("Inspection not found.");
		}

		// Step 2 : Get Test Results
		List<InspectionTestResult> testResults = resultRepo.findByInspection_InspectionId(inspectionId);

		// Step 3 : Convert to DTO
		List<InspectionTestResultResponse> response = new ArrayList<>();

		for (InspectionTestResult result : testResults) {
			response.add(InspectionTestResultMapper.toDto(result));
		}

		// Step 4 : Return Response
		return response;
	}

	@Override
	public InspectionTestResultResponse saveTestResult(int inspectionId, InspectionTestResultRequest request) {
		// Step 1 : Find Inspection
		Optional<InspectionDetails> optionalInspection = inspectionRepo.findById(inspectionId);

		if (optionalInspection.isEmpty()) {
			throw new RuntimeException("Inspection not found.");
		}

		// Step 2 : Get Inspection
		InspectionDetails inspection = optionalInspection.get();

		// Step 3 : Validation
		if (inspection.getStatus() != InspectionStatus.IN_PROGRESS) {
			throw new RuntimeException("Only inspections in progress can save test results.");
		}

		Optional<TestCatalog> optionalTest = testRepo.findById(request.getTestId());

		if (optionalTest.isEmpty()) {
			throw new RuntimeException("Test not found.");
		}

		TestCatalog test = optionalTest.get();

		if (resultRepo.findByInspection_InspectionIdAndTest_TestId(inspectionId, request.getTestId()).isPresent()) {

			throw new RuntimeException("Result for this test has already been submitted.");
		}

		// Step 6 : Create Test Result
		InspectionTestResult result = new InspectionTestResult();

		result.setInspection(inspection);
		result.setTest(test);

		result.setSampleType(request.getSampleType());
		result.setSampleDescription(request.getSampleDescription());
		result.setQuantitySampleTaken(request.getQuantitySampleTaken());
		result.setOutcome(request.getOutcome());
		result.setObservationNotes(request.getObservationNotes());
		result.setScoreAwarded(request.getScoreAwarded());
		result.setActionTaken(request.getActionTaken());
		result.setLabReferenceNo(request.getLabReferenceNo());

		result.setTestedAt(LocalDateTime.now());

		// Step 7 : Save
		InspectionTestResult savedResult = resultRepo.save(result);

		// Step 8 : Return DTO
		return InspectionTestResultMapper.toDto(savedResult);

	}

	@Override
	public InspectionResponse submitInspection(int inspectionId, String inspectorRemarks,
			InspectorRecommendation recommendation) {
		// Step 1 : Find Inspection
		Optional<InspectionDetails> optionalInspection = inspectionRepo.findById(inspectionId);

		if (optionalInspection.isEmpty()) {
			throw new RuntimeException("Inspection not found.");
		}

		// Step 2 : Get Inspection
		InspectionDetails inspection = optionalInspection.get();

		// Step 3 : Validation
		if (inspection.getStatus() != InspectionStatus.IN_PROGRESS) {
			throw new RuntimeException("Only inspections in progress can be submitted.");
		}

		// Step 4 : Get Test Results
		List<InspectionTestResult> testResults = resultRepo.findByInspection_InspectionId(inspectionId);

		if (testResults.isEmpty()) {
			throw new RuntimeException("Please complete at least one test before submitting.");
		}

		// Step 5 : Calculate Overall Score
		double totalScore = 0.0;

		for (InspectionTestResult result : testResults) {
			totalScore += result.getScoreAwarded();
		}

		double overallScore = totalScore / testResults.size();

		// Step 6 : Update Inspection
		inspection.setOverallInspectionScore(overallScore);
		inspection.setInspectorRemarks(inspectorRemarks);
		inspection.setRecommendation(recommendation);
		inspection.setCompletedAt(LocalDateTime.now());
		inspection.setStatus(InspectionStatus.REPORT_SUBMITTED);

		// Step 7 : Save Inspection
		InspectionDetails savedInspection = inspectionRepo.save(inspection);

		// Step 8 : Return DTO
		return InspectionMapper.toDto(savedInspection);

	}

	@Override
	public List<TestCatalogResponse> getAllTests() {
		List<TestCatalog> tests = testRepo.findByActiveTrue();

		List<TestCatalogResponse> response = new ArrayList<>();
		for (TestCatalog test : tests) {
			response.add(TestCatalogMapper.toDto(test));
		}

		return response;

	}

}
