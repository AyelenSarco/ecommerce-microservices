package com.ecommerce_microservices.sales_service.controller;


import com.ecommerce_microservices.sales_service.dto.ApiResponse;
import com.ecommerce_microservices.sales_service.dto.sale.SaleDTO;
import com.ecommerce_microservices.sales_service.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    @PostMapping("/create/{cartId}")
    public ResponseEntity<ApiResponse> createSale(@PathVariable Long cartId){
        SaleDTO saleDTO = saleService.createSale(cartId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Sale created successfully", saleDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getSale(@PathVariable Long id){
        SaleDTO saleDTO = saleService.getSale(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Sale", saleDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getSales(){
        List<SaleDTO> salesDTOS = saleService.getSales();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Sales", salesDTOS));
    }
}
