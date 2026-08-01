package com.gutfriendly.app.inspector.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.admin.dto.request.InspectionTestResultRequest;
import com.gutfriendly.app.admin.dto.request.SubmitInspectionRequest;
import com.gutfriendly.app.admin.dto.response.InspectionResponse;
import com.gutfriendly.app.admin.dto.response.InspectionTestResultResponse;
import com.gutfriendly.app.admin.dto.response.TestCatalogResponse;
import com.gutfriendly.app.inspector.service.InspectorInspectionService;

@RestController
@RequestMapping("/inspector")
public class InspectorInspectionController {

	final InspectorInspectionService service;

	InspectorInspectionController(InspectorInspectionService service) {
		this.service = service;
	}

	@GetMapping("/{inspectorId}/inspections")
	public Page<InspectionResponse> getAssignedInspections(@PathVariable int inspectorId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "inspectionDate") String sortBy,
			@RequestParam(defaultValue = "DESC") String direction) {

		return service.getAssignedInspections(inspectorId, page, size, sortBy, direction);
	}

	@GetMapping("/inspection/{inspectionId}")
	public InspectionResponse getInspectionById(@PathVariable int inspectionId) {

		return service.getInspectionById(inspectionId);
	}

	@PatchMapping("/inspection/{inspectionId}/start")
	public InspectionResponse startInspection(@PathVariable int inspectionId) {

		return service.startInspection(inspectionId);
	}

	@GetMapping("/tests")
	public List<TestCatalogResponse> getAllTests() {

		return service.getAllTests();
	}

	@PostMapping("/inspection/{inspectionId}/test-results")
	public InspectionTestResultResponse saveTestResult(@PathVariable int inspectionId,
			@RequestBody InspectionTestResultRequest request) {

		return service.saveTestResult(inspectionId, request);
	}

	@PatchMapping("/inspection/{inspectionId}/submit")
	public InspectionResponse submitInspection(@PathVariable int inspectionId,
			@RequestBody SubmitInspectionRequest request) {

		return service.submitInspection(inspectionId, request.getInspectorRemarks(), request.getRecommendation());
	}

	@GetMapping("/inspection/{inspectionId}/test-results")
	public List<InspectionTestResultResponse> getInspectionTestResults(@PathVariable int inspectionId) {

		return service.getInspectionTestResults(inspectionId);
	}
}
