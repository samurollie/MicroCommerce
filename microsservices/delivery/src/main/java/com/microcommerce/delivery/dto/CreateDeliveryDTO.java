package com.microcommerce.delivery.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateDeliveryDTO {
    @NotNull
    private String orderId;

    @NotNull
    private CreateAddressDTO address;
}
