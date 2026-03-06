package com.ecommerce_microservices.carts_service.dto.cart;

import com.ecommerce_microservices.carts_service.dto.cartItem.CartItemDTO;
import com.ecommerce_microservices.carts_service.model.CartStatusEnum;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponseDTO {

    private Long id;
    private double total;
    private CartStatusEnum status;
    private List<CartItemDTO> items = new ArrayList<>();
}
