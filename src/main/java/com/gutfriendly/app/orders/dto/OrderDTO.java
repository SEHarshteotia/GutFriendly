package com.gutfriendly.app.orders.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.gutfriendly.app.orders.enums.OrderStatus;
import com.gutfriendly.app.orders.enums.PaymentMethod;
import com.gutfriendly.app.orders.enums.PaymentStatus;

public class OrderDTO {

	private int orderId;
	private int shopId;
	private String shopName;
	private String deliveryAddress;
	private BigDecimal totalAmount;
	private OrderStatus orderStatus;
	private PaymentMethod paymentMethod;
	private PaymentStatus paymentStatus;
	private LocalDateTime orderedAt;
	private List<OrderItemDTO> items;
	private boolean reviewSubmitted;
	private Integer reviewId;

	public OrderDTO() {
	}

	public OrderDTO(int orderId, int shopId, String shopName, String deliveryAddress, BigDecimal totalAmount,
			OrderStatus orderStatus, PaymentMethod paymentMethod, PaymentStatus paymentStatus, LocalDateTime orderedAt,
			List<OrderItemDTO> items, boolean reviewSubmitted, Integer reviewId) {
		this.orderId = orderId;
		this.shopId = shopId;
		this.shopName = shopName;
		this.deliveryAddress = deliveryAddress;
		this.totalAmount = totalAmount;
		this.orderStatus = orderStatus;
		this.paymentMethod = paymentMethod;
		this.paymentStatus = paymentStatus;
		this.orderedAt = orderedAt;
		this.items = items;
		this.reviewSubmitted = reviewSubmitted;
		this.reviewId = reviewId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
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

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public LocalDateTime getOrderedAt() {
		return orderedAt;
	}

	public void setOrderedAt(LocalDateTime orderedAt) {
		this.orderedAt = orderedAt;
	}

	public List<OrderItemDTO> getItems() {
		return items;
	}

	public void setItems(List<OrderItemDTO> items) {
		this.items = items;
	}

	public boolean isReviewSubmitted() {
		return reviewSubmitted;
	}

	public void setReviewSubmitted(boolean reviewSubmitted) {
		this.reviewSubmitted = reviewSubmitted;
	}

	public Integer getReviewId() {
		return reviewId;
	}

	public void setReviewId(Integer reviewId) {
		this.reviewId = reviewId;
	}
}
