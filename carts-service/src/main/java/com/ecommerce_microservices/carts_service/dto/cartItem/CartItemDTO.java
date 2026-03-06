package com.ecommerce_microservices.carts_service.dto.cartItem;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {

    private Long id;
    private Long productId;
    private Integer quantity;
}
