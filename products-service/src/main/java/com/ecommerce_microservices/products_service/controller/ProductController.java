package com.ecommerce_microservices.products_service.controller;


import com.ecommerce_microservices.products_service.dto.ApiResponse;
import com.ecommerce_microservices.products_service.dto.ProductDTO;
import com.ecommerce_microservices.products_service.dto.validationStock.StockRequestDTO;
import com.ecommerce_microservices.products_service.dto.validationStock.StockValidationResponseDTO;
import com.ecommerce_microservices.products_service.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createProduct(@Validated @RequestBody ProductDTO productDTO) {

        ProductDTO dbProductDTO = productService.createProduct(productDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully", dbProductDTO));

    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Products", productService.getProducts()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Product", productService.getProductById(id)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteProductById(@PathVariable("id") Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.ok("Product deleted successfully");
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ApiResponse> editProduct(@PathVariable("id") Long id,
                                                   @Validated @RequestBody ProductDTO productDTO) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Product updated successfully", productService.updateProduct(id, productDTO)));
    }


    @PostMapping("/stock/validation")
    public ResponseEntity<ApiResponse<List<StockValidationResponseDTO>>> stockValidation(@Validated @RequestBody List<StockRequestDTO> stockValidationRequestDTO) {

        List<StockValidationResponseDTO> stockValidationResponseDTOS = productService.validateStock(stockValidationRequestDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Stock validation successfully", stockValidationResponseDTOS));

    }

    @PostMapping("/stock/update")
    public ResponseEntity<ApiResponse> updateStock( @Validated @RequestBody StockRequestDTO stockRequestDTO) {

        productService.updateStock(stockRequestDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Stock updated successfully", null));

    }

    @PostMapping("/stock/decrement")
    public ResponseEntity<ApiResponse> decrementStock(@Validated @RequestBody StockRequestDTO stockRequestDTO) {
        productService.decrementStock(stockRequestDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Stock decremented successfully", null));
    }



}
