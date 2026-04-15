package com.ecommerce_microservices.sales_service.service;

import com.ecommerce_microservices.sales_service.dto.sale.SaleDTO;
import com.ecommerce_microservices.sales_service.model.Sale;

import java.util.List;

public interface ISaleService {

    public SaleDTO createSale(Long cartId);
    public SaleDTO getSale(Long saleId);
    public List<SaleDTO> getSales();
}
