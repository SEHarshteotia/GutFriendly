package com.gutfriendly.app.orders.model;

import java.math.BigDecimal;

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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int orderItemId;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "order_id", nullable = false)
	    private UserOrders order;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "food_id", nullable = false)
	    private FoodItemsDetails food;

	    @Column(nullable = false)
	    private Integer quantity;

		@Column(nullable = false, precision = 10, scale = 2)
 		private BigDecimal unitPrice;

    	@Column(nullable = false, precision = 10, scale = 2)
    	private BigDecimal itemTotal;
	}


