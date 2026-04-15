package com.ecommerce_microservices.sales_service.mapper;


import com.ecommerce_microservices.sales_service.dto.sale.SaleDTO;
import com.ecommerce_microservices.sales_service.model.Sale;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    SaleDTO toSaleDTO(Sale sale);

}
