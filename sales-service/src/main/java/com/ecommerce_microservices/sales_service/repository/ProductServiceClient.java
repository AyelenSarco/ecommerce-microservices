package com.ecommerce_microservices.sales_service.repository;


import com.ecommerce_microservices.sales_service.dto.ApiResponse;
import com.ecommerce_microservices.sales_service.dto.product.StockRequestDTO;
import com.ecommerce_microservices.sales_service.dto.product.StockValidationResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="products-service")
public interface ProductServiceClient {

    @GetMapping("/products/stock/validation")
    public ApiResponse<List<StockValidationResponseDTO>> validateStock(List<StockRequestDTO> toValidate);

    @PostMapping("/products/stock/decrement")
    public ApiResponse<Void> decrementStock(@RequestBody List<StockRequestDTO> toDecrement);
}
