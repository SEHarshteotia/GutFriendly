package com.gutfriendly.app.vendor.mapper;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.gutfriendly.app.orders.enums.Status;
import com.gutfriendly.app.vendor.status.ShopOrderStatus;

public final class OrderStatusMapper {

	private static final List<Status> ACTIVE_STATUSES = List.of(
			Status.ORDER_PLACED,
			Status.ACCEPTED,
			Status.PREPARING,
			Status.OUT_FOR_DELIVERY);

	private OrderStatusMapper() {
	}

	public static List<Status> activeStatuses() {
		return ACTIVE_STATUSES;
	}

	public static Collection<Status> toCanonicalStatuses(Collection<ShopOrderStatus> statuses) {
		return statuses.stream().map(OrderStatusMapper::toCanonicalStatus).toList();
	}

	public static Status toCanonicalStatus(ShopOrderStatus status) {
		return switch (status) {
			case NEW -> Status.ORDER_PLACED;
			case ACCEPTED -> Status.ACCEPTED;
			case PREPARING -> Status.PREPARING;
			case OUT_FOR_DELIVERY -> Status.OUT_FOR_DELIVERY;
			case DELIVERED -> Status.DELIVERED;
			case CANCELLED -> Status.CANCELLED;
		};
	}

	public static ShopOrderStatus toShopOrderStatus(Status status) {
		return switch (status) {
			case ORDER_PLACED -> ShopOrderStatus.NEW;
			case ACCEPTED -> ShopOrderStatus.ACCEPTED;
			case PREPARING -> ShopOrderStatus.PREPARING;
			case OUT_FOR_DELIVERY -> ShopOrderStatus.OUT_FOR_DELIVERY;
			case DELIVERED -> ShopOrderStatus.DELIVERED;
			case CANCELLED -> ShopOrderStatus.CANCELLED;
		};
	}

	public static String statusLabel(ShopOrderStatus status) {
		return switch (status) {
			case NEW -> "New";
			case ACCEPTED -> "Accepted";
			case PREPARING -> "Preparing";
			case OUT_FOR_DELIVERY -> "Out for Delivery";
			case DELIVERED -> "Delivered";
			case CANCELLED -> "Cancelled";
		};
	}

	public static boolean isActiveFilter(String filter) {
		return "active".equalsIgnoreCase(filter);
	}

	public static ShopOrderStatus parseFilterStatus(String filter) {
		return ShopOrderStatus.valueOf(filter.toUpperCase());
	}

	public static List<ShopOrderStatus> vendorActiveStatuses() {
		return Arrays.asList(
				ShopOrderStatus.NEW,
				ShopOrderStatus.ACCEPTED,
				ShopOrderStatus.PREPARING,
				ShopOrderStatus.OUT_FOR_DELIVERY);
	}
}
