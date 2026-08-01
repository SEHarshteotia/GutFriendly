package com.gutfriendly.app.admin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gutfriendly.app.inspector.model.InspectionTestResult;

public interface InspectionTestResultRepo extends JpaRepository<InspectionTestResult, Integer> {
	
	Optional<InspectionTestResult> findByInspection_InspectionIdAndTest_TestId(
	        int inspectionId,
	        int testId);
	
	List<InspectionTestResult> findByInspection_InspectionId(
	        int inspectionId);

}
