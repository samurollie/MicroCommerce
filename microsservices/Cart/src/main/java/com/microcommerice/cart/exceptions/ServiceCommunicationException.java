package com.microcommerice.cart.exceptions;

public class ServiceCommunicationException extends RuntimeException {

    public ServiceCommunicationException(String message) {
        super(message);
    }

    public ServiceCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}