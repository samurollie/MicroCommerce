package com.microcommerce.orders.dto;

import com.microcommerce.orders.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusDTO {
    @NotNull
    private OrderStatus status;
}
