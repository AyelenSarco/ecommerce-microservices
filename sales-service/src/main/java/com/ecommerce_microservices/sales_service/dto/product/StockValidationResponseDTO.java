package com.ecommerce_microservices.sales_service.dto.product;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockValidationResponseDTO {

    private Long productId;
    private boolean available;
    private Double unitPrice;
    private int requestedQuantity;
    private int availableQuantity;

}
