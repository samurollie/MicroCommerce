package com.microcommerice.order.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemDto {
    private UUID id;
    private String productId;
    private String productName;
    private int quantity;
    private BigDecimal priceAtPurchase;
    private BigDecimal subtotal;
}
