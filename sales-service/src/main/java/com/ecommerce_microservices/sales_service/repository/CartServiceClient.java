package com.ecommerce_microservices.sales_service.repository;


import com.ecommerce_microservices.sales_service.config.FeignApacheHttpClientConfig;
import com.ecommerce_microservices.sales_service.dto.ApiResponse;
import com.ecommerce_microservices.sales_service.dto.cart.CartViewDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="carts-service",
        configuration = FeignApacheHttpClientConfig.class)
public interface CartServiceClient {

    @GetMapping("/carts/{id}")
    public ApiResponse<CartViewDTO> getCart(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.PATCH, value = "/carts/{id}/close")
    public ApiResponse<Void> closeCart(@PathVariable Long id);
}
