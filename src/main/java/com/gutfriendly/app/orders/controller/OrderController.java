package com.gutfriendly.app.orders.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gutfriendly.app.orders.dto.OrderDTO;
import com.gutfriendly.app.orders.dto.PlaceOrderDTO;
import com.gutfriendly.app.orders.enums.OrderStatus;
import com.gutfriendly.app.orders.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private final OrderService orderService;

	OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("/user/{userId}")
	public ResponseEntity<OrderDTO> placeOrder(@PathVariable int userId, @RequestBody PlaceOrderDTO request) {
		return ResponseEntity.ok(orderService.placeOrder(userId, request));
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<OrderDTO>> getMyOrders(@PathVariable int userId) {
		return ResponseEntity.ok(orderService.getMyOrders(userId));
	}

	@GetMapping("/user/{userId}/{orderId}")
	public ResponseEntity<OrderDTO> getOrderById(@PathVariable int userId, @PathVariable int orderId) {
		return ResponseEntity.ok(orderService.getOrderById(userId, orderId));
	}

	@PutMapping("/user/{userId}/{orderId}/cancel")
	public ResponseEntity<OrderDTO> cancelOrder(@PathVariable int userId, @PathVariable int orderId) {
		return ResponseEntity.ok(orderService.cancelOrder(userId, orderId));
	}

	@PutMapping("/{orderId}/status")
	public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable int orderId, @RequestParam OrderStatus status) {
		return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
	}
}
