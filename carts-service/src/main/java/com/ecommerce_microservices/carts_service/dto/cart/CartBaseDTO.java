package com.ecommerce_microservices.carts_service.dto.cart;

import com.ecommerce_microservices.carts_service.dto.cartItem.CartItemDTO;
import com.ecommerce_microservices.carts_service.model.CartStatusEnum;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartBaseDTO {
    private Long id;
    private double total;
    private CartStatusEnum status;
}
