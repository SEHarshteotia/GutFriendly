package com.gutfriendly.app.orders.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.admin.model.UserPaymentStatus;
import com.gutfriendly.app.orders.enums.Status;
import com.gutfriendly.app.user.model.UserDetails;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_orders")
public class UserOrders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status = Status.ORDER_PLACED;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserDetails user;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItems> orderItems = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shop_id", nullable = false)
	private ShopDetails shop;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_id")
	private UserPaymentStatus payment;

	@Column(name = "delivery_address", nullable = false, length = 500)
	private String deliveryAddress;

	@Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
	private BigDecimal totalAmount = BigDecimal.ZERO;

	@Column(name = "payment_method", nullable = false, length = 20)
	private String paymentMethod;

	@Column(name = "payment_status", nullable = false, length = 20)
	private String paymentStatus;

	@Column(nullable = true)
	private LocalDateTime orderedAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@Column(nullable = true)
	private LocalDateTime deliveredAt;

	@PrePersist
	public void onCreate() {
		LocalDateTime now = LocalDateTime.now();
		if (orderedAt == null) {
			orderedAt = now;
		}
		updatedAt = now;
	}

	@PreUpdate
	public void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public void addOrderItem(OrderItems item) {
		orderItems.add(item);
		item.setOrder(this);
	}

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

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public LocalDateTime getOrderedAt() {
		return orderedAt;
	}

	public void setOrderedAt(LocalDateTime orderedAt) {
		this.orderedAt = orderedAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LocalDateTime getDeliveredAt() {
		return deliveredAt;
	}

	public void setDeliveredAt(LocalDateTime deliveredAt) {
		this.deliveredAt = deliveredAt;
	}
}
