package com.ecommerce_microservices.sales_service.dto.product;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockRequestDTO {

    private Long productId;
    private int quantity;
}
