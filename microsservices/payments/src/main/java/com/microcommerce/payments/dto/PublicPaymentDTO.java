package com.microcommerce.payments.dto;

import lombok.Data;

@Data
public class PublicPaymentDTO {
    private Long id;
    private String orderId;
    private String paymentMethod;
    private float amount;
    private String status;
    private String createdAt;
}
