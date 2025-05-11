package com.microcommerice.cart.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "order-service", url = "${services.order.url:http://localhost:8083}")
public interface OrderClient {

    @PostMapping("/api/orders")
    ResponseEntity<OrderResponse> createOrder(@RequestHeader("X-User-ID") String userId,
                                              @RequestBody CreateOrderRequest request);

    // DTO inner classes
    @Getter
    @Setter
    class CreateOrderRequest {
        private List<OrderItemRequest> items;
        private BigDecimal totalAmount;
    }

    @Getter
    @Setter
    class OrderItemRequest {
        private Long productId;
        private String productName;
        private Integer quantity;
        private BigDecimal unitPrice;
    }

    @Getter
    @Setter
    class OrderResponse {
        private Long orderId;
        private String status;
    }
}