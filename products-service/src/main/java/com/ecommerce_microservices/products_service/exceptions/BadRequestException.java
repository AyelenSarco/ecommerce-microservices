package com.ecommerce_microservices.products_service.exceptions;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

}