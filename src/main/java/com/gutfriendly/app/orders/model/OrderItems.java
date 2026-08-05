package com.gutfriendly.app.orders.model;

import java.math.BigDecimal;

import com.gutfriendly.app.admin.model.FoodItemsDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItems {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderItemId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private UserOrders order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "food_id", nullable = false)
	private FoodItemsDetails food;

	@Column(nullable = false, length = 100)
	private String foodName;

	@Column(nullable = false)
	private Integer quantity;

	@Column(nullable = false)
	private Double price;

	@Column(name = "item_total", nullable = false, precision = 10, scale = 2)
	private BigDecimal itemTotal;

	public int getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}

	public UserOrders getOrder() {
		return order;
	}

	public void setOrder(UserOrders order) {
		this.order = order;
	}

	public FoodItemsDetails getFood() {
		return food;
	}

	public void setFood(FoodItemsDetails food) {
		this.food = food;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public BigDecimal getItemTotal() {
		return itemTotal;
	}

	public void setItemTotal(BigDecimal itemTotal) {
		this.itemTotal = itemTotal;
	}
}
