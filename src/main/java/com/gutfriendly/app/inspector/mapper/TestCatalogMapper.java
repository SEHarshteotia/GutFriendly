package com.gutfriendly.app.inspector.mapper;

import com.gutfriendly.app.admin.dto.response.TestCatalogResponse;
import com.gutfriendly.app.inspector.model.TestCatalog;

public class TestCatalogMapper {
	 public static TestCatalogResponse toDto(TestCatalog test) {

	        TestCatalogResponse dto = new TestCatalogResponse();

	        dto.setTestId(test.getTestId());
	        dto.setCategory(test.getCategory());
	        dto.setProductName(test.getProductName());
	        dto.setTestTitle(test.getTestTitle());
	        dto.setAdulterantName(test.getAdulterantName());
	        dto.setTestingMethod(test.getTestingMethod());
	        dto.setPositiveIndicator(test.getPositiveIndicator());
	        dto.setNegativeIndicator(test.getNegativeIndicator());
	        dto.setEquipmentRequired(test.getEquipmentRequired());
	        dto.setReferenceImagePure(test.getReferenceImagePure());
	        dto.setReferenceImageAdulterated(test.getReferenceImageAdulterated());
	        dto.setMaxScore(test.getMaxScore());

	        return dto;
	        
	 }

}
