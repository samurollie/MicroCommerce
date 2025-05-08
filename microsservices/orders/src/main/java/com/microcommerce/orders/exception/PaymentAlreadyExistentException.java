package com.microcommerce.orders.exception;

public class PaymentAlreadyExistentException extends RuntimeException {
    public PaymentAlreadyExistentException(String message) {
        super(message);
    }
}
