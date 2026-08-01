package com.gutfriendly.app.admin.dto.response;

import com.gutfriendly.app.admin.enums.FoodCategory;

public class TestCatalogResponse {
	  private int testId;

	    private FoodCategory category;

	    private String productName;

	    private String testTitle;

	    private String adulterantName;

	    private String testingMethod;

	    private String positiveIndicator;

	    private String negativeIndicator;

	    private String equipmentRequired;

	    private String referenceImagePure;

	    private String referenceImageAdulterated;

	    private Double maxScore;

		public TestCatalogResponse(int testId, FoodCategory category, String productName, String testTitle,
				String adulterantName, String testingMethod, String positiveIndicator, String negativeIndicator,
				String equipmentRequired, String referenceImagePure, String referenceImageAdulterated,
				Double maxScore) {
			super();
			this.testId = testId;
			this.category = category;
			this.productName = productName;
			this.testTitle = testTitle;
			this.adulterantName = adulterantName;
			this.testingMethod = testingMethod;
			this.positiveIndicator = positiveIndicator;
			this.negativeIndicator = negativeIndicator;
			this.equipmentRequired = equipmentRequired;
			this.referenceImagePure = referenceImagePure;
			this.referenceImageAdulterated = referenceImageAdulterated;
			this.maxScore = maxScore;
		}

		public TestCatalogResponse() {
			super();
			// TODO Auto-generated constructor stub
		}

		public int getTestId() {
			return testId;
		}

		public void setTestId(int testId) {
			this.testId = testId;
		}

		public FoodCategory getCategory() {
			return category;
		}

		public void setCategory(FoodCategory category) {
			this.category = category;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public String getTestTitle() {
			return testTitle;
		}

		public void setTestTitle(String testTitle) {
			this.testTitle = testTitle;
		}

		public String getAdulterantName() {
			return adulterantName;
		}

		public void setAdulterantName(String adulterantName) {
			this.adulterantName = adulterantName;
		}

		public String getTestingMethod() {
			return testingMethod;
		}

		public void setTestingMethod(String testingMethod) {
			this.testingMethod = testingMethod;
		}

		public String getPositiveIndicator() {
			return positiveIndicator;
		}

		public void setPositiveIndicator(String positiveIndicator) {
			this.positiveIndicator = positiveIndicator;
		}

		public String getNegativeIndicator() {
			return negativeIndicator;
		}

		public void setNegativeIndicator(String negativeIndicator) {
			this.negativeIndicator = negativeIndicator;
		}

		public String getEquipmentRequired() {
			return equipmentRequired;
		}

		public void setEquipmentRequired(String equipmentRequired) {
			this.equipmentRequired = equipmentRequired;
		}

		public String getReferenceImagePure() {
			return referenceImagePure;
		}

		public void setReferenceImagePure(String referenceImagePure) {
			this.referenceImagePure = referenceImagePure;
		}

		public String getReferenceImageAdulterated() {
			return referenceImageAdulterated;
		}

		public void setReferenceImageAdulterated(String referenceImageAdulterated) {
			this.referenceImageAdulterated = referenceImageAdulterated;
		}

		public Double getMaxScore() {
			return maxScore;
		}

		public void setMaxScore(Double maxScore) {
			this.maxScore = maxScore;
		}
		
		
		
		
	    
	

}
