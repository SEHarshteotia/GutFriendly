package com.gutfriendly.app.inspector.mapper;

import com.gutfriendly.app.admin.dto.response.InspectionTestResultResponse;
import com.gutfriendly.app.inspector.model.InspectionTestResult;
import com.gutfriendly.app.inspector.model.TestCatalog;

public class InspectionTestResultMapper {

	public static InspectionTestResultResponse toDto(InspectionTestResult result) {

		InspectionTestResultResponse dto = new InspectionTestResultResponse();

		dto.setResultId(result.getResultId());

		dto.setSampleType(result.getSampleType());

		dto.setSampleDescription(result.getSampleDescription());

		dto.setQuantitySampleTaken(result.getQuantitySampleTaken());

		dto.setOutcome(result.getOutcome());

		dto.setObservationNotes(result.getObservationNotes());

		dto.setScoreAwarded(result.getScoreAwarded());

		dto.setTestedAt(result.getTestedAt());

		dto.setActionTaken(result.getActionTaken());

		dto.setLabReferenceNo(result.getLabReferenceNo());

		dto.setTestId(result.getTest().getTestId());

		dto.setTestTitle(result.getTest().getTestTitle());

		dto.setProductName(result.getTest().getProductName());

		dto.setAdulterantName(result.getTest().getAdulterantName());

		return dto;

	}

}
