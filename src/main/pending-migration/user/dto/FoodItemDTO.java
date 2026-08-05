package com.gutfriendly.app.user.dto;

import java.math.BigDecimal;
import java.util.List;

public class FoodItemDTO {
	
	//FoodItemDTO sends food data back to the client, including generated fields such as foodId and shopId.

    private int foodId;
//    private int shopId;
    private String foodName;
    private BigDecimal price;
    private String foodDesc;
    private String foodCategory;
    private boolean available;
    private List<String> imageUrls;

    public FoodItemDTO() {
    }

    public FoodItemDTO(
            int foodId,
//            int shopId,
            String foodName,
            BigDecimal price,
            String foodDesc,
            String foodCategory,
            boolean available,
            List<String> imageUrls) {

        this.foodId = foodId;
//        this.shopId = shopId;
        this.foodName = foodName;
        this.price = price;
        this.foodDesc = foodDesc;
        this.foodCategory = foodCategory;
        this.available = available;
        this.imageUrls = imageUrls;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

//    public int getShopId() {
//        return shopId;
//    }

//    public void setShopId(int shopId) {
//        this.shopId = shopId;
//    }

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