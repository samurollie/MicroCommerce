package com.microcommerce.payments.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCardDTO {
    @NotNull
    private String cardHolderName;
    @NotNull
    private String cardNumber;
    @NotNull
    private String expirationDate;
    @NotNull
    private String cvv;
}
