package com.microcommerce.payments.external;

import com.microcommerce.payments.dto.PublicOrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", url = "${order-service.url}")
public interface OrderServiceClient {
    @GetMapping("/api/orders/{orderId}")
    PublicOrderDTO getOrder(@PathVariable String orderId);
}
