package com.ecommerce_microservices.carts_service.exception;

public class ApiError {
    private String message;
    private String status;

    public ApiError() {}

    public ApiError(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage(){
        return message;
    }

    public String getStatus(){
        return status;
    }

}
