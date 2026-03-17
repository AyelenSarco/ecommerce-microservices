package com.ecommerce_microservices.sales_service.controller;


import com.ecommerce_microservices.sales_service.dto.sale.SaleDTO;
import com.ecommerce_microservices.sales_service.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    @PutMapping("/create")
    public SaleDTO createSale(Long cartId){
        return saleService.createSale(cartId);
    }

    @GetMapping("/{id}")
    public SaleDTO getSale(@PathVariable Long id){
        return saleService.getSale(id);
    }

    @GetMapping("/all")
    public List<SaleDTO> getSales(){
        return saleService.getSales();
    }
}
