package com.microcommerce.payments.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PublicCardDTO {
    private String cardHolderName;
    private String cardNumber;
}
