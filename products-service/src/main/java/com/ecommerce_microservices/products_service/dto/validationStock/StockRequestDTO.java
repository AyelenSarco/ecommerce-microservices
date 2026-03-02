package com.ecommerce_microservices.products_service.dto.validationStock;


import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockRequestDTO {

    private Long productId;
    private int quantity;
}
