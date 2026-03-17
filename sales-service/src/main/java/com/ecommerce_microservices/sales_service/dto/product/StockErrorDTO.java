package com.ecommerce_microservices.sales_service.dto.product;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockErrorDTO {

    private Long productId;
    private Integer requestedQuantity;
    private Integer availableQuantity;
}
