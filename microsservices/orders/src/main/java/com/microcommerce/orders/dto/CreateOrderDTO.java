package com.microcommerce.orders.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDTO {
    private String customerId;
    private List<CreateOrderItemDTO> orderItems;
}
