package com.ecommerce_microservices.sales_service.exception;

public class ApiError {
    private String message;
    private String status;

    public ApiError(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public ApiError() {}

    public String getMessage() {
        return message;
    }
    public String getStatus() {
        return status;
    }
}
