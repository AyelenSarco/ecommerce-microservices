package com.ecommerce_microservices.products_service.exceptions;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
