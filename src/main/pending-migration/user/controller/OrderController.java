package com.gutfriendly.app.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gutfriendly.app.user.dto.OrderDTO;
import com.gutfriendly.app.user.dto.PlaceOrderDTO;
import com.gutfriendly.app.user.enums.OrderStatus;
import com.gutfriendly.app.user.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Place an order.
    @PostMapping("/user/{userId}")
    public ResponseEntity<OrderDTO> placeOrder(
            @PathVariable int userId,
            @RequestBody PlaceOrderDTO request) {

        return ResponseEntity.ok(
                orderService.placeOrder(
                        userId,
                        request
                )
        );
    }

    // Get all orders of one user.
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getMyOrders(
            @PathVariable int userId) {

        return ResponseEntity.ok(
                orderService.getMyOrders(userId)
        );
    }

    // Get one order.
    @GetMapping("/user/{userId}/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(
            @PathVariable int userId,
            @PathVariable int orderId) {

        return ResponseEntity.ok(
                orderService.getOrderById(
                        userId,
                        orderId
                )
        );
    }

    // Cancel an order while it is still PLACED.
    @PutMapping("/user/{userId}/{orderId}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(
            @PathVariable int userId,
            @PathVariable int orderId) {

        return ResponseEntity.ok(
                orderService.cancelOrder(
                        userId,
                        orderId
                )
        );
    }

    // Temporary vendor/admin-style status update.
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable int orderId,
            @RequestParam OrderStatus status) {

        return ResponseEntity.ok(
                orderService.updateOrderStatus(
                        orderId,
                        status
                )
        );
    }
}