package com.gutfriendly.app.inspector.model;

import com.gutfriendly.app.admin.enums.FoodCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "test_catalog")
public class TestCatalog {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int testId;

	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private FoodCategory category;

	    // Example: Milk, Turmeric, Tea, Honey
	    @Column(nullable = false, length = 100)
	    private String productName;

	    // Example: Detection of detergent in milk
	    @Column(nullable = false, length = 200)
	    private String testTitle;

	    // Example: Detergent, Water, Metanil Yellow
	    @Column(nullable = false, length = 150)
	    private String adulterantName;

	    @Column(columnDefinition = "TEXT")
	    private String testingMethod;

	    @Column(columnDefinition = "TEXT")
	    private String positiveIndicator;

	    @Column(columnDefinition = "TEXT")
	    private String negativeIndicator;

	    @Column(length = 300)
	    private String equipmentRequired;

	    @Column(length = 500)
	    private String referenceImagePure;

	    @Column(length = 500)
	    private String referenceImageAdulterated;

	    @Column(nullable = false)
	    private Double maxScore = 10.0;

	    @Column(nullable = false)
	    private Boolean active = true;

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

		public Boolean getActive() {
			return active;
		}

		public void setActive(Boolean active) {
			this.active = active;
		}
	    
	    

}
