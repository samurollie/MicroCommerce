package com.microcommerice.cart.exceptions;

public class CheckoutException extends RuntimeException {
    public CheckoutException(String message) {
        super(message);
    }
}