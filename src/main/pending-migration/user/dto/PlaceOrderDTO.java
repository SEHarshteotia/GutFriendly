package com.gutfriendly.app.user.dto;

import com.gutfriendly.app.user.enums.PaymentMethod;

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