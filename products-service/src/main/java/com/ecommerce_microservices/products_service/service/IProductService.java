package com.ecommerce_microservices.products_service.service;


import com.ecommerce_microservices.products_service.dto.ProductDTO;
import com.ecommerce_microservices.products_service.dto.validationStock.StockRequestDTO;
import com.ecommerce_microservices.products_service.dto.validationStock.StockValidationResponseDTO;

import java.util.List;


public interface IProductService {

    ProductDTO createProduct(ProductDTO productDTO);
    List<ProductDTO> getProducts();
    ProductDTO getProductById(Long id);
    void deleteProduct(Long id);
    ProductDTO updateProduct(Long id, ProductDTO productDTO);
    void updateStock(StockRequestDTO stockRequestDTO);


    List<StockValidationResponseDTO> validateStock(List<StockRequestDTO> items);
    void decrementStock(StockRequestDTO stockRequestDTO);


}
