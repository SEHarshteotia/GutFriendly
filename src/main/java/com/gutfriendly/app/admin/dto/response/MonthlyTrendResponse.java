package com.gutfriendly.app.admin.dto.response;

public class MonthlyTrendResponse {


    private String month;
    private Double averageScore;

    public MonthlyTrendResponse() {}

    public MonthlyTrendResponse(String month, Double averageScore) {
        this.month = month;
        this.averageScore = averageScore;
    }

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Double getAverageScore() {
		return averageScore;
	}

	public void setAverageScore(Double averageScore) {
		this.averageScore = averageScore;
	}

}
