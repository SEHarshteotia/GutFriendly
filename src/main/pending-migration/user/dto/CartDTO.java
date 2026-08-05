package com.gutfriendly.app.user.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartDTO {

    private int cartId;
    private int totalItems;
    private BigDecimal totalAmount;
    private List<CartItemDTO> items;

    public CartDTO() {
    }

    public CartDTO(
            int cartId,
            int totalItems,
            BigDecimal totalAmount,
            List<CartItemDTO> items) {

        this.cartId = cartId;
        this.totalItems = totalItems;
        this.totalAmount = totalAmount;
        this.items = items;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }
}