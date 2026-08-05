package com.gutfriendly.app.orders.dto;

import com.gutfriendly.app.orders.enums.PaymentMethod;

public class PlaceOrderDTO {

	private String deliveryAddress;
	private PaymentMethod paymentMethod;

	public PlaceOrderDTO() {
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
}
