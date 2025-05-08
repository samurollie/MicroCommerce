package com.microcommerce.payments.dto;

import com.microcommerce.payments.enums.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePaymentDTO {
    private PaymentMethod paymentMethod;
    private String orderId;
    private BigDecimal amount;
}
