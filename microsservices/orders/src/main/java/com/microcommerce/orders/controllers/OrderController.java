package com.microcommerce.orders.controllers;

import com.microcommerce.orders.dto.CreateOrderDTO;
import com.microcommerce.orders.models.OrderModel;
import com.microcommerce.orders.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderModel> createOrder(@RequestBody @Valid CreateOrderDTO createOrderDTO) {
        OrderModel orderModel = orderService.createOrder(createOrderDTO);

        return ResponseEntity.ok(orderModel);
    }

    @GetMapping
    public ResponseEntity<List<OrderModel>> listOrdersByUserId(@RequestParam("customerId") Long customerId) {
        List<OrderModel> orders = orderService.findByUserId(customerId);
        return ResponseEntity.ok(orders);
    }

}
