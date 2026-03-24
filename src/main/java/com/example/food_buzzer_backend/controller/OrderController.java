package com.example.food_buzzer_backend.controller;

import jakarta.validation.Valid;
import com.example.food_buzzer_backend.dto.order.OrderRequest;
import com.example.food_buzzer_backend.dto.order.OrderResponse;
import com.example.food_buzzer_backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(
        @RequestHeader("userId") Long userId, 
        @Valid @RequestBody OrderRequest request) {
            OrderResponse response = orderService.createOrder(userId, request);
            return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long orderId) {
        OrderResponse response = orderService.getOrderById(userId, orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/restaurant/orders")
    public ResponseEntity<List<OrderResponse>> getOrdersByRestaurant(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) List<String> statuses) {
        List<OrderResponse> orders = orderService.getOrdersByRestaurant(userId, statuses);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long orderId,
            @RequestParam String status) {
        OrderResponse response = orderService.updateOrderStatus(userId, orderId, status);
        return ResponseEntity.ok(response);
    }
}
