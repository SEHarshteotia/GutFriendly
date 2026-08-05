package com.gutfriendly.app.user.dto;

import java.util.List;

import com.gutfriendly.app.user.enums.ReviewKeyword;

public class ReviewRequestDTO {

    private int orderId;
    private int rating;
    private String comment;
    private List<ReviewKeyword> keywords;

    public ReviewRequestDTO() {
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ReviewKeyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<ReviewKeyword> keywords) {
        this.keywords = keywords;
    }
}