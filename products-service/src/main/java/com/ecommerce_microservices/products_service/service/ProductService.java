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
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    /**
     * This method update the stock of a product
     * **/
    @Override
    public void updateStock(StockRequestDTO stockRequestDTO) {

        Product dbProduct = productRepository.findById(stockRequestDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        dbProduct.setStock(stockRequestDTO.getQuantity());
        productRepository.save(dbProduct);

    }


    /**
     * This method checks the available stock of the products in the list
     * and returns a DTO with the required information.
     * **/
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
                    .unitPrice(dbProduct.getPrice())
                    .requestedQuantity(item.getQuantity())
                    .availableQuantity(dbProduct.getStock())
                    .build();

            stockValidationResponseDTOS.add(dtoResponse);

        }

        return stockValidationResponseDTOS;
    }

    /**
     * This method maps the product IDs from the stockRequest list to get all products from the database in one transaction.
     * Then, it maps the products by ID to find them in O(1).
     * After that, it validates that each product exists and has enough stock, and decreases the stock.
     * Finally, it saves all products with updated stock.
     **/

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void decrementStock(List<StockRequestDTO> stockRequests) {

        List<Long> productsIds = stockRequests.stream()
                        .map(StockRequestDTO::getProductId)
                        .toList();


        List<Product> products = productRepository.findAllById(productsIds);

        Map<Long, Product> productMap = products.stream()
                        .collect(Collectors.toMap(Product::getId, p -> p));


        //
        for (StockRequestDTO item : stockRequests) {
            Product product = productMap.get(item.getProductId());

            if (product == null) {
                throw new NotFoundException("Product not found: " + product.getCode());
            }
            if (product.getStock() < item.getQuantity()) {
                throw new InsufficientStockException("Stock exceeded. Product: " +  product.getCode());
            }

            product.setStock(product.getStock() - item.getQuantity());
        }

        productRepository.saveAll(products);

    }
}
