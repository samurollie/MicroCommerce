package com.microcommerce.orders.dto;

import lombok.Data;

@Data
public class CreateOrderItemDTO {
    private Long productId;
    private String name;
    private float price;
    private int quantity;
}
