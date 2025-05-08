package com.microcommerce.payments.exception;

public class PaymentAlreadyExistentException extends RuntimeException {
    public PaymentAlreadyExistentException(String message) {
        super(message);
    }
}
