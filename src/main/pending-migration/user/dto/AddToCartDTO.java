package com.gutfriendly.app.user.dto;

public class AddToCartDTO {

    private int foodId;
    private int quantity;

    public AddToCartDTO() {
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}