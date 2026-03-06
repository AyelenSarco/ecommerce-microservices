package com.ecommerce_microservices.carts_service.dto.cart;


import com.ecommerce_microservices.carts_service.dto.cartItem.CartItemViewDTO;
import com.ecommerce_microservices.carts_service.model.CartStatusEnum;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartViewDTO {

    private Long id;
    private Double total;
    private CartStatusEnum status;
    private List<CartItemViewDTO> cartItems = new ArrayList<>();
}
