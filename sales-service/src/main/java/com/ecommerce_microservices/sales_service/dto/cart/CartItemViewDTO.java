package com.ecommerce_microservices.sales_service.dto.cart;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemViewDTO {

    private Long id;
    private Long productId;
    private Integer quantity;
    private String productName;
    private String productBrand;
}
