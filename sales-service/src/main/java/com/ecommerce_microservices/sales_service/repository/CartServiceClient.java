package com.ecommerce_microservices.sales_service.repository;


import com.ecommerce_microservices.sales_service.dto.ApiResponse;
import com.ecommerce_microservices.sales_service.dto.cart.CartViewDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="carts-service")
public interface CartServiceClient {

    @GetMapping("/carts/{id}")
    public ApiResponse<CartViewDTO> getCart(@PathVariable Long id);
}
