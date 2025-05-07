package com.microcommerice.order.controller;

import com.microcommerice.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private String getUserId(Jwt jwt) {
        return jwt.getSubject();
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@AuthenticationPrincipal Jwt jwt,
                                                @Valid @RequestBody CreateOrderRequestDTO requestDTO) {
        String userId = getUserId(jwt);
        OrderDTO createdOrder = orderService.createOrder(userId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@AuthenticationPrincipal Jwt jwt,
                                                 @PathVariable UUID orderId) {
        String userId = getUserId(jwt);
        OrderDTO order = orderService.getOrderByIdAndUserId(orderId, userId);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getMyOrders(@AuthenticationPrincipal Jwt jwt) {
        String userId = getUserId(jwt);
        List<OrderDTO> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }
}
