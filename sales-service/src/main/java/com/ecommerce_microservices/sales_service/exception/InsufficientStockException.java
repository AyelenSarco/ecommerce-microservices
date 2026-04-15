package com.ecommerce_microservices.sales_service.exception;

import com.ecommerce_microservices.sales_service.dto.product.StockErrorDTO;

import java.util.List;

public class InsufficientStockException extends RuntimeException {

    private final List<StockErrorDTO> errors;

    public InsufficientStockException(List<StockErrorDTO> errors) {
        super("Some products have insufficient stock");
        this.errors = errors;
    }

    public List<StockErrorDTO> getErrors() {
        return errors;
    }
}