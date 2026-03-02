package com.ecommerce_microservices.products_service.service;


import com.ecommerce_microservices.products_service.dto.validationStock.StockValidationResponseDTO;
import com.ecommerce_microservices.products_service.exceptions.InsufficientStockException;
import com.ecommerce_microservices.products_service.mapper.ProductMapper;
import com.ecommerce_microservices.products_service.dto.ProductDTO;
import com.ecommerce_microservices.products_service.dto.validationStock.StockRequestDTO;
import com.ecommerce_microservices.products_service.exceptions.ConflictException;
import com.ecommerce_microservices.products_service.exceptions.NotFoundException;
import com.ecommerce_microservices.products_service.model.Product;
import com.ecommerce_microservices.products_service.repository.IProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService implements IProductService {

    private final IProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {

        if (productRepository.existsByCode(productDTO.getCode())){
            throw new ConflictException("Product with Code " + productDTO.getCode() + " already exists");
        }

        Product product = productMapper.toProduct(productDTO);

        product = productRepository.save(product);

        return null;
    }

    @Override
    public List<ProductDTO> getProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toProductDTO)
                .toList();
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id).map(productMapper::toProductDTO)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    @Override
    public void deleteProduct(Long id) {

        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Product not found");
        }

        productRepository.deleteById(id);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {

        Product dbProduct = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (productDTO.getCode() != null) { dbProduct.setCode(productDTO.getCode()); }
        if (productDTO.getName() != null) { dbProduct.setName(productDTO.getName()); }
        if (productDTO.getBrand() != null) { dbProduct.setBrand(productDTO.getBrand()); }
        if (productDTO.getPrice() != null) { dbProduct.setPrice(productDTO.getPrice()); }
        if (productDTO.getStock() != dbProduct.getStock()) { dbProduct.setStock(productDTO.getStock()); }

        productRepository.save(dbProduct);

        return productMapper.toProductDTO(dbProduct);
    }


    @Override
    public void updateStock(StockRequestDTO stockRequestDTO) {

        Product dbProduct = productRepository.findById(stockRequestDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        dbProduct.setStock(stockRequestDTO.getQuantity());
        productRepository.save(dbProduct);

    }


    @Override
    public List<StockValidationResponseDTO> validateStock(List<StockRequestDTO> items) {

        List<StockValidationResponseDTO> stockValidationResponseDTOS = new ArrayList<>();
        Product dbProduct;
        for (StockRequestDTO item : items) {

            dbProduct = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product with ID:" + item.getProductId()  + "not found"));

            boolean hasStock = dbProduct.getStock() >= item.getQuantity();

            StockValidationResponseDTO dtoResponse = StockValidationResponseDTO.builder()
                    .productId(item.getProductId())
                    .available(hasStock)
                    .requestedQuantity(item.getQuantity())
                    .availableQuantity(dbProduct.getStock())
                    .build();

            stockValidationResponseDTOS.add(dtoResponse);

        }

        return stockValidationResponseDTOS;
    }



    @Override
    public void decrementStock(StockRequestDTO stockRequestDTO) {
        Product dbProduct = productRepository.findById(stockRequestDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (dbProduct.getStock() < stockRequestDTO.getQuantity()) {
            throw new InsufficientStockException("Stock exceeded. Operation failed");
        }

        dbProduct.setStock(dbProduct.getStock() - stockRequestDTO.getQuantity());
        productRepository.save(dbProduct);
    }
}
