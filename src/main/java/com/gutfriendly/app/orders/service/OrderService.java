package com.gutfriendly.app.orders.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gutfriendly.app.orders.dto.OrderDTO;
import com.gutfriendly.app.orders.dto.OrderItemDTO;
import com.gutfriendly.app.orders.dto.PlaceOrderDTO;
import com.gutfriendly.app.admin.enums.ShopStatus;
import com.gutfriendly.app.admin.model.ShopDetails;
import com.gutfriendly.app.orders.enums.OrderStatus;
import com.gutfriendly.app.orders.enums.PaymentMethod;
import com.gutfriendly.app.orders.enums.PaymentStatus;
import com.gutfriendly.app.orders.enums.Status;
import com.gutfriendly.app.orders.mapper.OrderStatusMapper;
import com.gutfriendly.app.orders.model.OrderItems;
import com.gutfriendly.app.orders.model.UserOrders;
import com.gutfriendly.app.orders.repository.UserOrdersRepository;
import com.gutfriendly.app.reviews.model.ShopReview;
import com.gutfriendly.app.reviews.repository.ShopReviewRepository;
import com.gutfriendly.app.user.exception.BadRequestException;
import com.gutfriendly.app.user.exception.ConflictException;
import com.gutfriendly.app.user.exception.ResourceNotFoundException;
import com.gutfriendly.app.user.model.Cart;
import com.gutfriendly.app.user.model.CartItem;
import com.gutfriendly.app.user.model.UserDetails;
import com.gutfriendly.app.user.repository.CartRepository;
import com.gutfriendly.app.user.repository.UserRepo;

@Service
public class OrderService {

	private final UserOrdersRepository orderRepo;
	private final CartRepository cartRepo;
	private final UserRepo userRepo;
	private final ShopReviewRepository reviewRepo;

	OrderService(UserOrdersRepository orderRepo, CartRepository cartRepo, UserRepo userRepo,
			ShopReviewRepository reviewRepo) {
		this.orderRepo = orderRepo;
		this.cartRepo = cartRepo;
		this.userRepo = userRepo;
		this.reviewRepo = reviewRepo;
	}

	@Transactional
	public OrderDTO placeOrder(int userId, PlaceOrderDTO request) {
		if (request == null) {
			throw new BadRequestException("Order request is required");
		}
		if (request.getDeliveryAddress() == null || request.getDeliveryAddress().trim().isEmpty()) {
			throw new BadRequestException("Delivery address is required");
		}
		if (request.getPaymentMethod() == null) {
			throw new BadRequestException("Payment method is required");
		}

		UserDetails user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		Cart cart = cartRepo.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

		if (cart.getItems() == null || cart.getItems().isEmpty()) {
			throw new BadRequestException("Cannot place order because cart is empty");
		}

		ShopDetails shop = cart.getItems().get(0).getFood().getShop();
		validateShopAcceptsOrders(shop);

		UserOrders order = new UserOrders();
		order.setUser(user);
		order.setShop(shop);
		order.setDeliveryAddress(request.getDeliveryAddress().trim());
		order.setPaymentMethod(request.getPaymentMethod().name());
		order.setPaymentStatus(PaymentStatus.PENDING.name());
		order.setStatus(Status.ORDER_PLACED);

		BigDecimal totalAmount = BigDecimal.ZERO;

		for (CartItem cartItem : cart.getItems()) {
			if (cartItem.getFood() == null) {
				throw new ResourceNotFoundException("Food item linked to cart was not found");
			}
			if (!cartItem.getFood().isAvailable()) {
				throw new BadRequestException(cartItem.getFood().getFoodName() + " is currently unavailable");
			}
			if (cartItem.getQuantity() <= 0) {
				throw new BadRequestException("Cart item quantity must be greater than zero");
			}

			BigDecimal itemTotal = cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

			OrderItems orderItem = new OrderItems();
			orderItem.setFood(cartItem.getFood());
			orderItem.setFoodName(cartItem.getFood().getFoodName());
			orderItem.setPrice(cartItem.getUnitPrice().doubleValue());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setItemTotal(itemTotal);
			order.addOrderItem(orderItem);

			totalAmount = totalAmount.add(itemTotal);
		}

		order.setTotalAmount(totalAmount);
		UserOrders savedOrder = orderRepo.save(order);

		cart.getItems().clear();
		cartRepo.save(cart);

		return convertToDTO(savedOrder);
	}

	@Transactional(readOnly = true)
	public List<OrderDTO> getMyOrders(int userId) {
		if (!userRepo.existsById(userId)) {
			throw new ResourceNotFoundException("User not found");
		}
		return orderRepo.findOrdersByUserId(userId).stream().map(this::convertToDTO).toList();
	}

	@Transactional(readOnly = true)
	public OrderDTO getOrderById(int userId, int orderId) {
		UserOrders order = orderRepo.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));
		validateOrderOwnership(order, userId);
		return convertToDTO(order);
	}

	@Transactional
	public OrderDTO cancelOrder(int userId, int orderId) {
		UserOrders order = orderRepo.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));
		validateOrderOwnership(order, userId);

		if (OrderStatusMapper.fromCanonical(order.getStatus()) != OrderStatus.PLACED) {
			throw new ConflictException("Order cannot be cancelled after it has been accepted");
		}

		order.setStatus(Status.CANCELLED);

		if (PaymentMethod.ONLINE.name().equals(order.getPaymentMethod())
				&& PaymentStatus.SUCCESS.name().equals(order.getPaymentStatus())) {
			order.setPaymentStatus(PaymentStatus.REFUNDED.name());
		}

		return convertToDTO(orderRepo.save(order));
	}

	@Transactional
	public OrderDTO updateOrderStatus(int orderId, OrderStatus newStatus) {
		if (newStatus == null) {
			throw new BadRequestException("Order status is required");
		}

		UserOrders order = orderRepo.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		OrderStatus currentStatus = OrderStatusMapper.fromCanonical(order.getStatus());

		if (currentStatus == OrderStatus.CANCELLED) {
			throw new ConflictException("Cancelled order status cannot be changed");
		}
		if (currentStatus == OrderStatus.DELIVERED) {
			throw new ConflictException("Delivered order status cannot be changed");
		}
		if (newStatus == OrderStatus.CANCELLED) {
			throw new ConflictException("Use the cancel order API to cancel an order");
		}

		validateStatusTransition(currentStatus, newStatus);
		order.setStatus(OrderStatusMapper.toCanonical(newStatus));

		if (newStatus == OrderStatus.DELIVERED && PaymentMethod.COD.name().equals(order.getPaymentMethod())) {
			order.setPaymentStatus(PaymentStatus.SUCCESS.name());
		}

		return convertToDTO(orderRepo.save(order));
	}

	private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
		if (!OrderStatusMapper.isValidTransition(currentStatus, newStatus)) {
			throw new ConflictException(
					"Invalid order status transition from " + currentStatus + " to " + newStatus);
		}
	}

	private void validateShopAcceptsOrders(ShopDetails shop) {
		if (shop.getStatus() != ShopStatus.VERIFIED) {
			throw new BadRequestException("This shop is not verified and cannot accept orders");
		}
		if (Boolean.TRUE.equals(shop.getBlocked())) {
			throw new BadRequestException("This shop is not accepting orders");
		}
		if (!Boolean.TRUE.equals(shop.getOnlineOrdersEnabled())) {
			throw new BadRequestException("This shop is not accepting online orders");
		}
		if (!Boolean.TRUE.equals(shop.getIsOpen())) {
			throw new BadRequestException("This shop is currently closed");
		}
	}

	private void validateOrderOwnership(UserOrders order, int userId) {
		if (order.getUser().getUser_id() != userId) {
			throw new ConflictException("This order does not belong to the user");
		}
	}

	private OrderDTO convertToDTO(UserOrders order) {
		List<OrderItemDTO> itemDTOs = new ArrayList<>();

		if (order.getOrderItems() != null) {
			for (OrderItems item : order.getOrderItems()) {
				if (item == null) {
					continue;
				}
				int foodId = item.getFood() != null ? item.getFood().getFoodId() : 0;
				itemDTOs.add(new OrderItemDTO(
						item.getOrderItemId(),
						foodId,
						item.getFoodName(),
						BigDecimal.valueOf(item.getPrice()),
						item.getQuantity(),
						item.getItemTotal()));
			}
		}

		Optional<ShopReview> reviewOptional = reviewRepo.findByOrderOrderId(order.getOrderId());
		boolean reviewSubmitted = reviewOptional.isPresent();
		Integer reviewId = reviewOptional.map(ShopReview::getReviewId).orElse(null);

		return new OrderDTO(
				order.getOrderId(),
				order.getShop().getShopId(),
				order.getShop().getShopName(),
				order.getDeliveryAddress(),
				order.getTotalAmount(),
				OrderStatusMapper.fromCanonical(order.getStatus()),
				PaymentMethod.valueOf(order.getPaymentMethod()),
				PaymentStatus.valueOf(order.getPaymentStatus()),
				order.getOrderedAt(),
				itemDTOs,
				reviewSubmitted,
				reviewId);
	}
}
