package com.gutfriendly.app.inspector.model;

import java.time.LocalDateTime;

import com.gutfriendly.app.admin.enums.FoodSampleType;
import com.gutfriendly.app.admin.enums.InspectionActionTaken;
import com.gutfriendly.app.admin.enums.TestOutcome;
import jakarta.persistence.*;

    @Entity
	@Table(name = "inspection_test_result")
	public class InspectionTestResult {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int resultId;

	    // Parent Inspection
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "inspection_id", nullable = false)
	    private InspectionDetails inspection;

	    // Which FSSAI test was performed
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "test_id", nullable = false)
	    private TestCatalog test;

	    
	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private FoodSampleType sampleType;
	    
	    
	    // Sample details
	    @Column(length = 200)
	    private String sampleDescription;

	    // Example : 5ml, 10gm, 1 tsp
	    @Column(length = 50)
	    private String quantitySampleTaken;

	    // PASS / FAIL / INCONCLUSIVE / NOT_TESTED
	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private TestOutcome outcome;

	    // Inspector observation
	    @Column(length = 1000)
	    private String observationNotes;

	    // Score obtained in this test
	    @Column(nullable = false)
	    private Double scoreAwarded = 0.0;

	    // Time when this particular test was completed
	    @Column(nullable = false)
	    private LocalDateTime testedAt;

	    // Action taken after the test
	    @Enumerated(EnumType.STRING)
	    private InspectionActionTaken actionTaken =
	            InspectionActionTaken.NONE;
	    
	    
	   

	    // If sample sent to laboratory
	    @Column(length = 100)
	    private String labReferenceNo;




		public int getResultId() {
			return resultId;
		}




		public void setResultId(int resultId) {
			this.resultId = resultId;
		}




		public InspectionDetails getInspection() {
			return inspection;
		}




		public void setInspection(InspectionDetails inspection) {
			this.inspection = inspection;
		}




		public TestCatalog getTest() {
			return test;
		}




		public void setTest(TestCatalog test) {
			this.test = test;
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


