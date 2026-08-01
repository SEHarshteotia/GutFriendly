package com.gutfriendly.app.admin.dto.request;

import com.gutfriendly.app.admin.enums.FoodSampleType;
import com.gutfriendly.app.admin.enums.InspectionActionTaken;
import com.gutfriendly.app.admin.enums.TestOutcome;

public class InspectionTestResultRequest {
	private int testId;

	private FoodSampleType sampleType;

	private String sampleDescription;

	private String quantitySampleTaken;

	private TestOutcome outcome;

	private String observationNotes;

	private Double scoreAwarded;

	private InspectionActionTaken actionTaken;

	private String labReferenceNo;

	public int getTestId() {
		return testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
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
