package com.gutfriendly.app.user.dto;

import java.math.BigDecimal;

public class CartItemDTO {

    private int cartItemId;
    private int foodId;
    private String foodName;
    private String imageUrl;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal itemTotal;

    public CartItemDTO() {
    }

    public CartItemDTO(
            int cartItemId,
            int foodId,
            String foodName,
            String imageUrl,
            BigDecimal unitPrice,
            int quantity,
            BigDecimal itemTotal) {

        this.cartItemId = cartItemId;
        this.foodId = foodId;
        this.foodName = foodName;
        this.imageUrl = imageUrl;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.itemTotal = itemTotal;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(BigDecimal itemTotal) {
        this.itemTotal = itemTotal;
    }
}