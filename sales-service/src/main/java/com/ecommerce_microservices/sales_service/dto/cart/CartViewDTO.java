package com.ecommerce_microservices.sales_service.dto.cart;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartViewDTO {

    private Long id;
    private Double total;
    private CartStatusEnum status;
    private List<CartItemViewDTO> cartItems = new ArrayList<>();
}
