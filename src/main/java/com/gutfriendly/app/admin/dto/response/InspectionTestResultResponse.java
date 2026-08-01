package com.gutfriendly.app.admin.dto.response;

import java.time.LocalDateTime;

import com.gutfriendly.app.admin.enums.FoodSampleType;
import com.gutfriendly.app.admin.enums.InspectionActionTaken;
import com.gutfriendly.app.admin.enums.TestOutcome;

public class InspectionTestResultResponse {
	

	    private int resultId;

	    private int testId;

	    private String testTitle;

	    private String productName;

	    private String adulterantName;

	    private FoodSampleType sampleType;

	    private String sampleDescription;

	    private String quantitySampleTaken;

	    private TestOutcome outcome;

	    private String observationNotes;

	    private Double scoreAwarded;

	    private LocalDateTime testedAt;

	    private InspectionActionTaken actionTaken;

	    private String labReferenceNo;

		public int getResultId() {
			return resultId;
		}

		public void setResultId(int resultId) {
			this.resultId = resultId;
		}

		public int getTestId() {
			return testId;
		}

		public void setTestId(int testId) {
			this.testId = testId;
		}

		public String getTestTitle() {
			return testTitle;
		}

		public void setTestTitle(String testTitle) {
			this.testTitle = testTitle;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public String getAdulterantName() {
			return adulterantName;
		}

		public void setAdulterantName(String adulterantName) {
			this.adulterantName = adulterantName;
		}

		public FoodSampleType getSampleType() {
			return sampleType;
		}

		public void setSampleType(FoodSampleType sampleType) {
			this.sampleType = sampleType;
		}

		public String getSampleDescription() {
			return sampleDescription;
		}

		public void setSampleDescription(String sampleDescription) {
			this.sampleDescription = sampleDescription;
		}

		public String getQuantitySampleTaken() {
			return quantitySampleTaken;
		}

		public void setQuantitySampleTaken(String quantitySampleTaken) {
			this.quantitySampleTaken = quantitySampleTaken;
		}

		public TestOutcome getOutcome() {
			return outcome;
		}

		public void setOutcome(TestOutcome outcome) {
			this.outcome = outcome;
		}

		public String getObservationNotes() {
			return observationNotes;
		}

		public void setObservationNotes(String observationNotes) {
			this.observationNotes = observationNotes;
		}

		public Double getScoreAwarded() {
			return scoreAwarded;
		}

		public void setScoreAwarded(Double scoreAwarded) {
			this.scoreAwarded = scoreAwarded;
		}

		public LocalDateTime getTestedAt() {
			return testedAt;
		}

		public void setTestedAt(LocalDateTime testedAt) {
			this.testedAt = testedAt;
		}

		public InspectionActionTaken getActionTaken() {
			return actionTaken;
		}

		public void setActionTaken(InspectionActionTaken actionTaken) {
			this.actionTaken = actionTaken;
		}

		public String getLabReferenceNo() {
			return labReferenceNo;
		}

		public void setLabReferenceNo(String labReferenceNo) {
			this.labReferenceNo = labReferenceNo;
		}
	    
	    

	

}
