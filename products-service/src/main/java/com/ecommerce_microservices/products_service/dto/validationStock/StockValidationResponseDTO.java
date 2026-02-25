package com.ecommerce_microservices.products_service.dto.validationStock;


import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockValidationResponseDTO {

    private Long productId;
    private boolean available;
    private int requestedQuantity;
    private int availableQuantity;

}
