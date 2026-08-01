package com.gutfriendly.app.admin.dto.response;

public class CategoryPerformanceResponse {
	   private String category;
	    private Double averageScore;

	    public CategoryPerformanceResponse() {
	    }

	    public CategoryPerformanceResponse(String category, Double averageScore) {
	        this.category = category;
	        this.averageScore = averageScore;
	    }

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public Double getAverageScore() {
			return averageScore;
		}

		public void setAverageScore(Double averageScore) {
			this.averageScore = averageScore;
		}

}
