package com.ecommerce_microservices.carts_service.repository;


import com.ecommerce_microservices.carts_service.dto.ApiResponse;
import com.ecommerce_microservices.carts_service.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="products-service")
public interface ProductServiceClient {

    @GetMapping("/products/{id}")
    public ApiResponse<ProductDTO> getProductById(@PathVariable Long id);


}
