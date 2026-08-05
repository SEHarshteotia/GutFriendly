package com.gutfriendly.app.admin.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.gutfriendly.app.admin.dto.response.InspectionResponse;
import com.gutfriendly.app.admin.dto.response.InspectorSummaryResponse;
import com.gutfriendly.app.admin.enums.InspectionStatus;

public interface InspectionService {
	
	// Get all inspections
	 public Page<InspectionResponse> getAllInspections(
            int page,
            int size,
            String sortBy,
            String direction);

    // Get one inspection
    InspectionResponse getInspectionById(int inspectionId);

    // Search by inspection status
    public Page<InspectionResponse> getInspectionsByStatus(
            InspectionStatus status,
            int page,
            int size,
            String sortBy,
            String direction);

    // Get inspections of a shop
    public Page<InspectionResponse> getInspectionsByShop(
            int shopId,
            int page,
            int size,
            String sortBy,
            String direction);

    // Get inspections assigned to an inspector
    public Page<InspectionResponse> getInspectionsByInspector(
            int inspectorId,
            int page,
            int size,
            String sortBy,
            String direction);
    
    // List inspectors for admin assignment
    List<InspectorSummaryResponse> getAllInspectors();

    // Assign / Change Inspector
    public InspectionResponse assignInspector(
            int inspectionId,
            int inspectorId);

    // Admin reviews submitted inspection
    public InspectionResponse reviewInspection(
            int inspectionId
           );

    // Admin approves inspector report
   public  InspectionResponse approveInspection(
            int inspectionId);

    // Admin rejects inspection report
    public InspectionResponse rejectInspection(
            int inspectionId,
            String rejectionReason);

    // Send inspection back to inspector
    public InspectionResponse sendForReInspection(
            int inspectionId,
            String reason);
	

}
