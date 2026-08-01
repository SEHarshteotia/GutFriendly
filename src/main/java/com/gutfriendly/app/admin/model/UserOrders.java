package com.gutfriendly.app.admin.model;

import java.time.LocalDateTime;
import java.util.List;

import com.gutfriendly.app.admin.enums.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
 
@Entity
@Table(name="user_orders")
public class UserOrders {
	 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 private int  orderId;
	 
	@Enumerated(EnumType.STRING)
	@Column(nullable =false)
	private Status status = Status.ORDER_PLACED;
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id",nullable=false)
	 private UserDetails user;
	
	@OneToMany(
		    mappedBy = "order",
		    cascade = CascadeType.ALL,
		    orphanRemoval = true
		)
		private List<OrderItems> orderItems;
	 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shop_id", nullable = false)
	private ShopDetails shop;
	 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_id")
	private UserPaymentStatus payment;
	
	@Column(nullable = true)
	private LocalDateTime orderedAt;
	
	@Column(nullable = true)
	private LocalDateTime deliveredAt;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public UserDetails getUser() {
		return user;
	}

	public void setUser(UserDetails user) {
		this.user = user;
	}

	public List<OrderItems> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItems> orderItems) {
		this.orderItems = orderItems;
	}

	public ShopDetails getShop() {
		return shop;
	}

	public void setShop(ShopDetails shop) {
		this.shop = shop;
	}

	public UserPaymentStatus getPayment() {
		return payment;
	}

	public void setPayment(UserPaymentStatus payment) {
		this.payment = payment;
	}

	public LocalDateTime getOrderedAt() {
		return orderedAt;
	}

	public void setOrderedAt(LocalDateTime orderedAt) {
		this.orderedAt = orderedAt;
	}

	public LocalDateTime getDeliveredAt() {
		return deliveredAt;
	}

	public void setDeliveredAt(LocalDateTime deliveredAt) {
		this.deliveredAt = deliveredAt;
	}
	


}