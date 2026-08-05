package com.gutfriendly.app.vendor.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.gutfriendly.app.orders.enums.Status;
import com.gutfriendly.app.orders.model.OrderItems;
import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.orders.model.UserOrders;
import com.gutfriendly.app.orders.repository.UserOrdersRepository;
import com.gutfriendly.app.vendor.dto.ShopOrderDTO;
import com.gutfriendly.app.vendor.dto.ShopOrderItemDTO;
import com.gutfriendly.app.vendor.dto.UpdateOrderStatusRequestDTO;
import com.gutfriendly.app.vendor.mapper.OrderStatusMapper;
import com.gutfriendly.app.vendor.status.ShopOrderStatus;

@Service
public class ShopOrderService {

	private final VendorContextService contextService;
	private final UserOrdersRepository orderRepository;

	ShopOrderService(VendorContextService contextService, UserOrdersRepository orderRepository) {
		this.contextService = contextService;
		this.orderRepository = orderRepository;
	}

	@Transactional(readOnly = true)
	public long getActiveOrderCount(Integer vendorId, Long shopId) {
		ShopDetails shop = contextService.findShop(vendorId, shopId);
		return orderRepository.countByShopAndStatusIn(shop, OrderStatusMapper.activeStatuses());
	}

	@Transactional(readOnly = true)
	public List<ShopOrderDTO> listOrders(Integer vendorId, Long shopId, String filter) {
		ShopDetails shop = contextService.findShop(vendorId, shopId);
		List<UserOrders> orders;

		if (OrderStatusMapper.isActiveFilter(filter)) {
			orders = orderRepository.findByShopAndStatusInOrderByOrderedAtDesc(shop,
					OrderStatusMapper.activeStatuses());
		} else if (filter != null && !filter.isBlank()) {
			try {
				Status status = OrderStatusMapper.toCanonicalStatus(OrderStatusMapper.parseFilterStatus(filter));
				orders = orderRepository.findByShopAndStatusInOrderByOrderedAtDesc(shop, List.of(status));
			} catch (IllegalArgumentException ex) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order status filter");
			}
		} else {
			orders = orderRepository.findByShopOrderByOrderedAtDesc(shop);
		}

		return orders.stream().map(this::toOrderDTO).toList();
	}

	@Transactional(readOnly = true)
	public ShopOrderDTO getOrder(Integer vendorId, Long shopId, Long orderId) {
		ShopDetails shop = contextService.findShop(vendorId, shopId);
		UserOrders order = orderRepository.findByOrderIdAndShop(orderId.intValue(), shop)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
		return toOrderDTO(order);
	}

	@Transactional
	public ShopOrderDTO updateOrderStatus(Integer vendorId, Long shopId, Long orderId,
			UpdateOrderStatusRequestDTO request) {
		if (request.getStatus() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status is required");
		}

		ShopDetails shop = contextService.findShop(vendorId, shopId);
		UserOrders order = orderRepository.findByOrderIdAndShop(orderId.intValue(), shop)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

		Status currentCanonical = order.getStatus();
		Status newCanonical = OrderStatusMapper.toCanonicalStatus(request.getStatus());
		com.gutfriendly.app.orders.enums.OrderStatus current = com.gutfriendly.app.orders.mapper.OrderStatusMapper
				.fromCanonical(currentCanonical);
		com.gutfriendly.app.orders.enums.OrderStatus next = com.gutfriendly.app.orders.mapper.OrderStatusMapper
				.fromCanonical(newCanonical);

		if (current == com.gutfriendly.app.orders.enums.OrderStatus.CANCELLED) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Cancelled orders cannot be updated");
		}
		if (current == com.gutfriendly.app.orders.enums.OrderStatus.DELIVERED) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Delivered orders cannot be updated");
		}
		if (next == com.gutfriendly.app.orders.enums.OrderStatus.CANCELLED) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Order cancellation is not supported through status updates");
		}
		if (!com.gutfriendly.app.orders.mapper.OrderStatusMapper.isValidTransition(current, next)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Invalid order status transition from " + current + " to " + next);
		}

		order.setStatus(newCanonical);
		return toOrderDTO(orderRepository.save(order));
	}

	private ShopOrderDTO toOrderDTO(UserOrders order) {
		List<OrderItems> orderItems = order.getOrderItems() == null ? List.of() : order.getOrderItems();
		List<ShopOrderItemDTO> items = orderItems.stream().map(this::toOrderItemDTO).toList();
		ShopOrderStatus status = OrderStatusMapper.toShopOrderStatus(order.getStatus());

		return new ShopOrderDTO(
				(long) order.getOrderId(),
				"ORD-" + order.getOrderId(),
				status,
				OrderStatusMapper.statusLabel(status),
				calculateTotal(orderItems),
				order.getOrderedAt(),
				minutesAgo(order.getOrderedAt()),
				items);
	}

	private ShopOrderItemDTO toOrderItemDTO(OrderItems item) {
		Long menuItemId = item.getFood() != null ? (long) item.getFood().getFoodId() : null;
		String itemName = item.getFood() != null ? item.getFood().getFoodName() : "Item";
		BigDecimal unitPrice = item.getPrice() != null ? BigDecimal.valueOf(item.getPrice()) : BigDecimal.ZERO;

		return new ShopOrderItemDTO(
				(long) item.getOrderItemId(),
				menuItemId,
				itemName,
				item.getQuantity(),
				unitPrice);
	}

	private BigDecimal calculateTotal(List<OrderItems> items) {
		BigDecimal total = BigDecimal.ZERO;
		for (OrderItems item : items) {
			if (item.getPrice() != null && item.getQuantity() != null) {
				total = total.add(BigDecimal.valueOf(item.getPrice()).multiply(BigDecimal.valueOf(item.getQuantity())));
			}
		}
		return total;
	}

	private long minutesAgo(LocalDateTime createdAt) {
		if (createdAt == null) {
			return 0;
		}
		return Math.max(0, Duration.between(createdAt, LocalDateTime.now()).toMinutes());
	}
}
