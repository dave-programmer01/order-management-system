package com.orderms.order.exception;

// Thrown when the Product Service is unreachable or returns a 5xx error
public class ProductServiceUnavailableException extends RuntimeException {
    public ProductServiceUnavailableException(String message) {
        super(message);
    }
}