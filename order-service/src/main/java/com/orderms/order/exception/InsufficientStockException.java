package com.orderms.order.exception;

// Thrown when a product does not have enough stock for the requested quantity
public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}