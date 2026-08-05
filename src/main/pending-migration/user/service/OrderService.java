package com.gutfriendly.app.user.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gutfriendly.app.user.dto.OrderDTO;
import com.gutfriendly.app.user.dto.OrderItemDTO;
import com.gutfriendly.app.user.dto.PlaceOrderDTO;
import com.gutfriendly.app.user.enums.OrderStatus;
import com.gutfriendly.app.user.enums.PaymentMethod;
import com.gutfriendly.app.user.enums.PaymentStatus;
import com.gutfriendly.app.user.exception.BadRequestException;
import com.gutfriendly.app.user.exception.ConflictException;
import com.gutfriendly.app.user.exception.ResourceNotFoundException;
import com.gutfriendly.app.user.model.Cart;
import com.gutfriendly.app.user.model.CartItem;
import com.gutfriendly.app.user.model.CustomerOrder;
import com.gutfriendly.app.user.model.OrderItem;
import com.gutfriendly.app.user.model.ShopReview;
import com.gutfriendly.app.user.model.UserDetails;
import com.gutfriendly.app.user.repository.CartRepository;
import com.gutfriendly.app.user.repository.CustomerOrderRepository;
import com.gutfriendly.app.user.repository.ShopReviewRepository;
import com.gutfriendly.app.user.repository.UserRepo;

@Service
public class OrderService {

    @Autowired
    private CustomerOrderRepository orderRepo;

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ShopReviewRepository reviewRepo;

    // Converts the user's current cart into an order.
    @Transactional
    public OrderDTO placeOrder(
            int userId,
            PlaceOrderDTO request) {

        if (request == null) {
            throw new BadRequestException(
                    "Order request is required"
            );
        }

        if (request.getDeliveryAddress() == null ||
                request.getDeliveryAddress()
                        .trim()
                        .isEmpty()) {

            throw new BadRequestException(
                    "Delivery address is required"
            );
        }

        if (request.getPaymentMethod() == null) {
            throw new BadRequestException(
                    "Payment method is required"
            );
        }

        UserDetails user =
                userRepo.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                ));

        Cart cart =
                cartRepo.findByUser(user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cart not found"
                                ));

        if (cart.getItems() == null ||
                cart.getItems().isEmpty()) {

            throw new BadRequestException(
                    "Cannot place order because cart is empty"
            );
        }

        CustomerOrder order =
                new CustomerOrder();

        order.setUser(user);

        // All cart items belong to the same shop.
        order.setShop(
                cart.getItems()
                        .get(0)
                        .getFood()
                        .getShop()
        );

        order.setDeliveryAddress(
                request.getDeliveryAddress().trim()
        );

        order.setPaymentMethod(
                request.getPaymentMethod()
        );

        order.setOrderStatus(
                OrderStatus.PLACED
        );

        // Payment remains pending when the order is placed.
        order.setPaymentStatus(
                PaymentStatus.PENDING
        );

        BigDecimal totalAmount =
                BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {

            if (cartItem.getFood() == null) {
                throw new ResourceNotFoundException(
                        "Food item linked to cart was not found"
                );
            }

            if (!cartItem.getFood().isAvailable()) {
                throw new BadRequestException(
                        cartItem.getFood().getFoodName()
                                + " is currently unavailable"
                );
            }

            if (cartItem.getQuantity() <= 0) {
                throw new BadRequestException(
                        "Cart item quantity must be greater than zero"
                );
            }

            BigDecimal itemTotal =
                    cartItem.getUnitPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            cartItem.getQuantity()
                                    )
                            );

            OrderItem orderItem =
                    new OrderItem();

            orderItem.setFood(
                    cartItem.getFood()
            );

            orderItem.setFoodName(
                    cartItem.getFood().getFoodName()
            );

            orderItem.setUnitPrice(
                    cartItem.getUnitPrice()
            );

            orderItem.setQuantity(
                    cartItem.getQuantity()
            );

            orderItem.setItemTotal(
                    itemTotal
            );

            order.addItem(orderItem);

            totalAmount =
                    totalAmount.add(itemTotal);
        }

        order.setTotalAmount(
                totalAmount
        );

        CustomerOrder savedOrder =
                orderRepo.save(order);

        // Cart is cleared only after the order is saved.
        cart.getItems().clear();

        cartRepo.save(cart);

        return convertToDTO(savedOrder);
    }

    // Returns all orders of one user, newest first.
    @Transactional(readOnly = true)
    public List<OrderDTO> getMyOrders(
            int userId) {

        if (!userRepo.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found"
            );
        }

        return orderRepo
                .findOrdersByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Returns one order after checking ownership.
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(
            int userId,
            int orderId) {

        CustomerOrder order =
                orderRepo.findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"
                                ));

        validateOrderOwnership(
                order,
                userId
        );

        return convertToDTO(order);
    }

    // Cancels an order only while it is still in PLACED state.
    @Transactional
    public OrderDTO cancelOrder(
            int userId,
            int orderId) {

        CustomerOrder order =
                orderRepo.findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"
                                ));

        validateOrderOwnership(
                order,
                userId
        );

        if (order.getOrderStatus()
                != OrderStatus.PLACED) {

            throw new ConflictException(
                    "Order cannot be cancelled after it has been accepted"
            );
        }

        order.setOrderStatus(
                OrderStatus.CANCELLED
        );

        /*
         * If an online payment had already succeeded,
         * mark it as refunded after cancellation.
         */
        if (order.getPaymentMethod()
                == PaymentMethod.ONLINE
                &&
                order.getPaymentStatus()
                        == PaymentStatus.SUCCESS) {

            order.setPaymentStatus(
                    PaymentStatus.REFUNDED
            );
        }

        CustomerOrder cancelledOrder =
                orderRepo.save(order);

        return convertToDTO(cancelledOrder);
    }

    /*
     * Updates an order's status.
     *
     * This is currently a temporary method.
     * Later it should be called only from the
     * vendor/admin order management module.
     */
    @Transactional
    public OrderDTO updateOrderStatus(
            int orderId,
            OrderStatus newStatus) {

        if (newStatus == null) {
            throw new BadRequestException(
                    "Order status is required"
            );
        }

        CustomerOrder order =
                orderRepo.findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"
                                ));

        OrderStatus currentStatus =
                order.getOrderStatus();

        if (currentStatus
                == OrderStatus.CANCELLED) {

            throw new ConflictException(
                    "Cancelled order status cannot be changed"
            );
        }

        if (currentStatus
                == OrderStatus.DELIVERED) {

            throw new ConflictException(
                    "Delivered order status cannot be changed"
            );
        }

        if (newStatus
                == OrderStatus.CANCELLED) {

            throw new ConflictException(
                    "Use the cancel order API to cancel an order"
            );
        }

        validateStatusTransition(
                currentStatus,
                newStatus
        );

        order.setOrderStatus(
                newStatus
        );

        /*
         * For Cash on Delivery, payment is completed
         * only after the order reaches DELIVERED.
         */
        if (newStatus
                == OrderStatus.DELIVERED
                &&
                order.getPaymentMethod()
                        == PaymentMethod.COD) {

            order.setPaymentStatus(
                    PaymentStatus.SUCCESS
            );
        }

        CustomerOrder updatedOrder =
                orderRepo.save(order);

        return convertToDTO(updatedOrder);
    }

    /*
     * Prevents invalid order-status jumps.
     *
     * Expected flow:
     * PLACED -> ACCEPTED -> PREPARING
     * -> OUT_FOR_DELIVERY -> DELIVERED
     */
    private void validateStatusTransition(
            OrderStatus currentStatus,
            OrderStatus newStatus) {

        boolean validTransition = false;

        switch (currentStatus) {

            case PLACED:
                validTransition =
                        newStatus
                                == OrderStatus.ACCEPTED;
                break;

            case ACCEPTED:
                validTransition =
                        newStatus
                                == OrderStatus.PREPARING;
                break;

            case PREPARING:
                validTransition =
                        newStatus
                                == OrderStatus.OUT_FOR_DELIVERY;
                break;

            case OUT_FOR_DELIVERY:
                validTransition =
                        newStatus
                                == OrderStatus.DELIVERED;
                break;

            default:
                validTransition = false;
        }

        if (!validTransition) {
            throw new ConflictException(
                    "Invalid order status transition from "
                            + currentStatus
                            + " to "
                            + newStatus
            );
        }
    }

    // Prevents one user from viewing or cancelling another user's order.
    private void validateOrderOwnership(
            CustomerOrder order,
            int userId) {

        if (order.getUser().getUser_id()
                != userId) {

            throw new ConflictException(
                    "This order does not belong to the user"
            );
        }
    }

    // Converts CustomerOrder entity into OrderDTO.
    private OrderDTO convertToDTO(
            CustomerOrder order) {

        List<OrderItemDTO> itemDTOs =
                new ArrayList<>();

        if (order.getItems() != null) {

            for (OrderItem item : order.getItems()) {

                if (item == null) {
                    continue;
                }

                int foodId = 0;

                if (item.getFood() != null) {
                    foodId =
                            item.getFood().getFoodId();
                }

                OrderItemDTO itemDTO =
                        new OrderItemDTO(
                                item.getOrderItemId(),
                                foodId,
                                item.getFoodName(),
                                item.getUnitPrice(),
                                item.getQuantity(),
                                item.getItemTotal()
                        );

                itemDTOs.add(itemDTO);
            }
        }

        Optional<ShopReview> reviewOptional =
                reviewRepo.findByOrderOrderId(
                        order.getOrderId()
                );

        boolean reviewSubmitted =
                reviewOptional.isPresent();

        Integer reviewId =
                reviewOptional
                        .map(ShopReview::getReviewId)
                        .orElse(null);

        return new OrderDTO(
                order.getOrderId(),
                order.getShop().getShopId(),
                order.getShop().getShopName(),
                order.getDeliveryAddress(),
                order.getTotalAmount(),
                order.getOrderStatus(),
                order.getPaymentMethod(),
                order.getPaymentStatus(),
                order.getOrderedAt(),
                itemDTOs,
                reviewSubmitted,
                reviewId
        );
    }
}