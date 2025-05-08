package com.microcommerce.delivery.exception;

public class DeliveryAlreadyExistentException extends RuntimeException {
    public DeliveryAlreadyExistentException(String message) {
        super(message);
    }
}
