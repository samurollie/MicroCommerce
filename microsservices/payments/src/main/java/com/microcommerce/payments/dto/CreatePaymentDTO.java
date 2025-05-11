package com.microcommerce.payments.dto;

import com.microcommerce.payments.enums.PaymentMethod;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePaymentDTO {
    @NotNull
    private PaymentMethod paymentMethod;
    @NotNull
    private String orderId;
    @NotNull
    private BigDecimal amount;
    @Valid
    private CreateCardDTO cardInfo;
}
