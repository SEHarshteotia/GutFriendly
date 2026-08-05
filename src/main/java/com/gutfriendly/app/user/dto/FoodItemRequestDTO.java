package com.gutfriendly.app.user.dto;

import java.math.BigDecimal;
import java.util.List;

public class FoodItemRequestDTO {
	
	//FoodItemRequestDTO receives data from Postman/React.

    private String foodName;
    private BigDecimal price;
    private String foodDesc;
    private String foodCategory;
    private boolean available = true;
    private List<String> imageUrls;

    public FoodItemRequestDTO() {
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getFoodDesc() {
        return foodDesc;
    }

    public void setFoodDesc(String foodDesc) {
        this.foodDesc = foodDesc;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}