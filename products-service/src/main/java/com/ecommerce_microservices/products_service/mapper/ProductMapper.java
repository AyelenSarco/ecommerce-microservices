package com.ecommerce_microservices.products_service.mapper;

import com.ecommerce_microservices.products_service.dto.ProductDTO;
import com.ecommerce_microservices.products_service.model.Product;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ProductMapper {


    ProductDTO toProductDTO(Product product) ;
    Product toProduct(ProductDTO productDTO);
}
