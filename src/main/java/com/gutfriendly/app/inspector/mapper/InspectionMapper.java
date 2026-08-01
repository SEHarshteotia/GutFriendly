package com.gutfriendly.app.inspector.mapper;

import com.gutfriendly.app.admin.dto.response.InspectionResponse;
import com.gutfriendly.app.inspector.model.InspectionDetails;


public class InspectionMapper {
	
	public static InspectionResponse toDto(InspectionDetails inspection) {
		   InspectionResponse dto = new InspectionResponse();

		  		  dto.setInspectionId(inspection.getInspectionId());
                dto.setShopId(inspection.getShop().getShopId());
		           dto.setShopName(inspection.getShop().getShopName());
                   dto.setVendorId(inspection.getVendor().getVendorId());
                   dto.setVendorName(inspection.getVendor().getFirstName()+" "+inspection.getVendor().getLastName());
                   if (inspection.getInspector() != null) {

                	    dto.setInspectorId(
                	            inspection.getInspector().getInspectorId());

                	    dto.setInspectorName(
                	            inspection.getInspector().getFirstName()
                	            + " "
                	            + inspection.getInspector().getLastName());
                	}

		          

		           dto.setInspectionDate(inspection.getInspectionDate());

		           dto.setCompletedAt(inspection.getCompletedAt());

		           dto.setStatus(inspection.getStatus());

		           dto.setOverallInspectionScore(
		                   inspection.getOverallInspectionScore());

		           dto.setRecommendation(
		                   inspection.getRecommendation());

		           dto.setInspectorRemarks(
		                   inspection.getInspectorRemarks());

		           dto.setAdminRemarks(
		                   inspection.getAdminRemarks());

		           dto.setReviewedByAdmin(
		                   inspection.getReviewedByAdmin());

		           dto.setReviewedAt(
		                   inspection.getReviewedAt());

		           return dto;
		       

		   
	       


}
		   }

