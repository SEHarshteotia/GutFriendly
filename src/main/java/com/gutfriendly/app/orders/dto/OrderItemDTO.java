package com.gutfriendly.app.orders.dto;

import java.math.BigDecimal;

public class OrderItemDTO {

	private int orderItemId;
	private int foodId;
	private String foodName;
	private BigDecimal unitPrice;
	private int quantity;
	private BigDecimal itemTotal;

	public OrderItemDTO() {
	}

	public OrderItemDTO(int orderItemId, int foodId, String foodName, BigDecimal unitPrice, int quantity,
			BigDecimal itemTotal) {
		this.orderItemId = orderItemId;
		this.foodId = foodId;
		this.foodName = foodName;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
		this.itemTotal = itemTotal;
	}

	public int getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
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
