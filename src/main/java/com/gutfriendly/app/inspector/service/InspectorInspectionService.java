package com.gutfriendly.app.inspector.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.gutfriendly.app.admin.dto.request.InspectionTestResultRequest;
import com.gutfriendly.app.admin.dto.response.InspectionResponse;
import com.gutfriendly.app.admin.dto.response.InspectionTestResultResponse;
import com.gutfriendly.app.admin.dto.response.TestCatalogResponse;
import com.gutfriendly.app.admin.enums.InspectorRecommendation;

public interface InspectorInspectionService {
	// Get all inspections assigned to an inspector
	Page<InspectionResponse> getAssignedInspections(int inspectorId, int page, int size, String sortBy,
			String direction);

	// Get one inspection
	InspectionResponse getInspectionById(int inspectionId);

	// Start inspection
	InspectionResponse startInspection(int inspectionId);

	// Get all available predefined tests
	List<TestCatalogResponse> getAllTests();

	// Get all submitted test results for an inspection
	List<InspectionTestResultResponse> getInspectionTestResults(int inspectionId);

	// Save one test result
	InspectionTestResultResponse saveTestResult(int inspectionId, InspectionTestResultRequest request);

	// Submit completed inspection report
	InspectionResponse submitInspection(int inspectionId, String inspectorRemarks,
			InspectorRecommendation recommendation);
}