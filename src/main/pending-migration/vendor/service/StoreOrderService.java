package com.gutfriendly.app.vendor.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.gutfriendly.app.vendor.dto.UpdateOrderStatusRequestDTO;
import com.gutfriendly.app.vendor.dto.StoreOrderDTO;
import com.gutfriendly.app.vendor.dto.StoreOrderItemDTO;
import com.gutfriendly.app.vendor.model.StoreOrder;
import com.gutfriendly.app.vendor.model.StoreOrderItem;
import com.gutfriendly.app.vendor.model.Store;
import com.gutfriendly.app.vendor.repository.StoreOrderRepo;
import com.gutfriendly.app.vendor.status.StoreOrderStatus;

/**
 * Lists and updates vendor orders for a shop, including active order counts.
 */
@Service
public class StoreOrderService {

	private static final List<StoreOrderStatus> ACTIVE_STATUSES = Arrays.asList(
			StoreOrderStatus.NEW,
			StoreOrderStatus.PREPARING,
			StoreOrderStatus.OUT_FOR_DELIVERY);

	private final VendorContextService contextService;
	private final StoreOrderRepo orderRepository;

	StoreOrderService(VendorContextService contextService, StoreOrderRepo orderRepository) {
		this.contextService = contextService;
		this.orderRepository = orderRepository;
	}

	/**
	 * Counts orders in active statuses for a shop.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @return number of active orders
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor or shop not found
	 */
	@Transactional(readOnly = true)
	public long getActiveOrderCount(Integer vendorId, Long shopId) {
		Store store = contextService.findShop(vendorId, shopId);
		return orderRepository.countByStoreAndStatusIn(store, ACTIVE_STATUSES);
	}

	/**
	 * Lists orders for a shop with optional status filtering.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @param filter {@code active} for all active statuses, a {@link StoreOrderStatus} name, or null for all
	 * @return order DTOs in reverse chronological order
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor or shop not found
	 * @throws ResponseStatusException with {@link HttpStatus#BAD_REQUEST} if filter is an invalid status name
	 */
	@Transactional(readOnly = true)
	public List<StoreOrderDTO> listOrders(Integer vendorId, Long shopId, String filter) {
		Store store = contextService.findShop(vendorId, shopId);
		List<StoreOrder> orders;

		if ("active".equalsIgnoreCase(filter)) {
			orders = orderRepository.findByStoreAndStatusInOrderByCreatedAtDesc(store, ACTIVE_STATUSES);
		} else if (filter != null && !filter.isBlank()) {
			try {
				StoreOrderStatus status = StoreOrderStatus.valueOf(filter.toUpperCase());
				orders = orderRepository.findByStoreAndStatusInOrderByCreatedAtDesc(store, List.of(status));
			} catch (IllegalArgumentException ex) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order status filter");
			}
		} else {
			orders = orderRepository.findByStoreOrderByCreatedAtDesc(store);
		}

		return orders.stream().map(this::toOrderDTO).toList();
	}

	/**
	 * Returns a single order with line items.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @param orderId the order's primary key
	 * @return order DTO with items
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor, shop, or order not found
	 */
	@Transactional(readOnly = true)
	public StoreOrderDTO getOrder(Integer vendorId, Long shopId, Long orderId) {
		Store store = contextService.findShop(vendorId, shopId);
		StoreOrder order = orderRepository.findByOrderIdAndStore(orderId, store)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
		return toOrderDTO(order);
	}

	/**
	 * Updates the status of an order.
	 *
	 * @param vendorId the vendor's primary key
	 * @param shopId the shop's primary key
	 * @param orderId the order's primary key
	 * @param request status update payload; {@code status} is required
	 * @return updated order DTO
	 * @throws ResponseStatusException with {@link HttpStatus#NOT_FOUND} if vendor, shop, or order not found
	 * @throws ResponseStatusException with {@link HttpStatus#BAD_REQUEST} if status is missing
	 */
	@Transactional
	public StoreOrderDTO updateOrderStatus(Integer vendorId, Long shopId, Long orderId,
			UpdateOrderStatusRequestDTO request) {
		if (request.getStatus() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status is required");
		}

		Store store = contextService.findShop(vendorId, shopId);
		StoreOrder order = orderRepository.findByOrderIdAndStore(orderId, store)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

		order.setStatus(request.getStatus());
		return toOrderDTO(orderRepository.save(order));
	}

	private StoreOrderDTO toOrderDTO(StoreOrder order) {
		List<StoreOrderItemDTO> items = order.getItems() == null
				? List.of()
				: order.getItems().stream().map(this::toOrderItemDTO).toList();

		return new StoreOrderDTO(
				order.getOrderId(),
				order.getOrderNumber(),
				order.getStatus(),
				getStatusLabel(order.getStatus()),
				order.getTotalAmount(),
				order.getCreatedAt(),
				minutesAgo(order.getCreatedAt()),
				items);
	}

	private StoreOrderItemDTO toOrderItemDTO(StoreOrderItem item) {
		Long menuItemId = item.getMenuItem() != null ? item.getMenuItem().getItemId() : null;
		return new StoreOrderItemDTO(
				item.getOrderItemId(),
				menuItemId,
				item.getItemName(),
				item.getQuantity(),
				item.getUnitPrice());
	}

	private String getStatusLabel(StoreOrderStatus status) {
		return switch (status) {
			case NEW -> "New";
			case PREPARING -> "Preparing";
			case OUT_FOR_DELIVERY -> "Out for Delivery";
			case DELIVERED -> "Delivered";
			case CANCELLED -> "Cancelled";
		};
	}

	private long minutesAgo(LocalDateTime createdAt) {
		if (createdAt == null) {
			return 0;
		}
		return Math.max(0, Duration.between(createdAt, LocalDateTime.now()).toMinutes());
	}
}
