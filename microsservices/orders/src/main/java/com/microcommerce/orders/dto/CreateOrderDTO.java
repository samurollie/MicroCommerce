package com.microcommerce.orders.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDTO {
    @NotNull
    private String customerId;
    @NotNull
    private List<CreateOrderItemDTO> orderItems;
    @NotNull
    private double totalPrice;
}
