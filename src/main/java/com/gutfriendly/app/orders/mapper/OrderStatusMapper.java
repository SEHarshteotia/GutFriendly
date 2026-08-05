package com.gutfriendly.app.orders.mapper;

import com.gutfriendly.app.orders.enums.OrderStatus;
import com.gutfriendly.app.orders.enums.Status;

public final class OrderStatusMapper {

	private OrderStatusMapper() {
	}

	public static Status toCanonical(OrderStatus status) {
		return switch (status) {
			case PLACED -> Status.ORDER_PLACED;
			case ACCEPTED -> Status.ACCEPTED;
			case PREPARING -> Status.PREPARING;
			case OUT_FOR_DELIVERY -> Status.OUT_FOR_DELIVERY;
			case DELIVERED -> Status.DELIVERED;
			case CANCELLED -> Status.CANCELLED;
		};
	}

	public static OrderStatus fromCanonical(Status status) {
		return switch (status) {
			case ORDER_PLACED -> OrderStatus.PLACED;
			case ACCEPTED -> OrderStatus.ACCEPTED;
			case PREPARING -> OrderStatus.PREPARING;
			case OUT_FOR_DELIVERY -> OrderStatus.OUT_FOR_DELIVERY;
			case DELIVERED -> OrderStatus.DELIVERED;
			case CANCELLED -> OrderStatus.CANCELLED;
		};
	}

	public static boolean isValidTransition(OrderStatus currentStatus, OrderStatus newStatus) {
		return switch (currentStatus) {
			case PLACED -> newStatus == OrderStatus.ACCEPTED;
			case ACCEPTED -> newStatus == OrderStatus.PREPARING;
			case PREPARING -> newStatus == OrderStatus.OUT_FOR_DELIVERY;
			case OUT_FOR_DELIVERY -> newStatus == OrderStatus.DELIVERED;
			default -> false;
		};
	}
}
