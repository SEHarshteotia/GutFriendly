package com.gutfriendly.app.admin.controller;


import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.admin.dto.request.InspectionRejectionRequest;
import com.gutfriendly.app.admin.dto.request.ReInspectionRequest;
import com.gutfriendly.app.admin.dto.request.ReviewInspectionRequest;
import com.gutfriendly.app.admin.dto.response.InspectionResponse;
import com.gutfriendly.app.admin.enums.InspectionStatus;
import com.gutfriendly.app.admin.service.InspectionService;

@RestController()
@RequestMapping("/admin/inspections")
public class AdminInspectionController {
	
	private  final InspectionService  service  ;


	AdminInspectionController(InspectionService service) {
		this.service = service;
	}
	
	
	@GetMapping()
	public Page<InspectionResponse> getAllInspections(
			   @RequestParam(defaultValue = "0") int page,
               @RequestParam(defaultValue = "10") int size,
               @RequestParam(defaultValue = "inspectionDate") String sortBy,
               @RequestParam(defaultValue = "DESC") String direction){
		
		return service.getAllInspections(page, size, sortBy, direction);
		}
	
	
	
	
	@GetMapping("/{inspectionId}")
	public InspectionResponse getInspectionById(@PathVariable  int inspectionId){
		
		return service.getInspectionById(inspectionId);
		}
	
	
	@GetMapping("/status/{status}")
	public Page<InspectionResponse> getInspectionsByStatus(
			   @PathVariable InspectionStatus status,
			   @RequestParam(defaultValue = "0") int page,
               @RequestParam(defaultValue = "10") int size,
               @RequestParam(defaultValue = "inspectionDate") String sortBy,
               @RequestParam(defaultValue = "DESC") String direction){
		
		return service.getInspectionsByStatus(status,page, size, sortBy, direction);
		}
	
	@GetMapping("/shop/{shopId}")
	public Page<InspectionResponse> getInspectionsByShop(
			   @PathVariable int  shopId,
			   @RequestParam(defaultValue = "0") int page,
               @RequestParam(defaultValue = "10") int size,
               @RequestParam(defaultValue = "inspectionDate") String sortBy,
               @RequestParam(defaultValue = "DESC") String direction){
		
		return service.getInspectionsByShop(shopId,page, size, sortBy, direction);
		}
	
	@GetMapping("/inspector/{inspectorId}")
	public Page<InspectionResponse>getInspectionsByInspector (
			   @PathVariable int  inspectorId,
			   @RequestParam(defaultValue = "0") int page,
               @RequestParam(defaultValue = "10") int size,
               @RequestParam(defaultValue = "inspectionDate") String sortBy,
               @RequestParam(defaultValue = "DESC") String direction){
		
		return service.getInspectionsByInspector(inspectorId,page, size, sortBy, direction);
		}

	@PatchMapping("/{inspectionId}/assign/{inspectorId}")
	public InspectionResponse assignInspector(
	        @PathVariable int inspectionId,
	        @PathVariable int inspectorId) {

	    return service.assignInspector(inspectionId, inspectorId);
	}
	
	@PatchMapping("/{inspectionId}/review")
	public InspectionResponse reviewInspection(
	        @PathVariable int inspectionId
	       ) {

	    return service.reviewInspection(
	            inspectionId
	            );
	}
	
	@PatchMapping("/{inspectionId}/approve")
	public InspectionResponse approveInspection(
	        @PathVariable int inspectionId) {

	    return service.approveInspection(inspectionId);
	}
	
	@PatchMapping("/{inspectionId}/reject")
	public InspectionResponse rejectInspection(
	        @PathVariable int inspectionId,
	        @RequestBody InspectionRejectionRequest  request  ) {

	    return service.rejectInspection(inspectionId,request.getInspectionRejectionReason());
	}
	
	@PatchMapping("/{inspectionId}/reinspection")
	public InspectionResponse sendForReInspection (
	        @PathVariable int inspectionId,
	        @RequestBody ReInspectionRequest  request  ) {

	    return service.sendForReInspection(inspectionId,request.getReInspectionRequestReason());
	}
	
	
	
	
	

}
