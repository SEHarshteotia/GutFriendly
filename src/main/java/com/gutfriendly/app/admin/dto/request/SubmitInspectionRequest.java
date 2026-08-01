package com.gutfriendly.app.admin.dto.request;

import com.gutfriendly.app.admin.enums.InspectorRecommendation;

public class SubmitInspectionRequest {

    private String inspectorRemarks;

    private InspectorRecommendation recommendation;

    public String getInspectorRemarks() {
        return inspectorRemarks;
    }

    public void setInspectorRemarks(String inspectorRemarks) {
        this.inspectorRemarks = inspectorRemarks;
    }

    public InspectorRecommendation getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(
            InspectorRecommendation recommendation) {
        this.recommendation = recommendation;
    }
}


