package com.ecommerce_microservices.carts_service.mapper;


import com.ecommerce_microservices.carts_service.dto.cart.CartBaseDTO;
import com.ecommerce_microservices.carts_service.dto.cart.CartViewDTO;
import com.ecommerce_microservices.carts_service.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target="cartItems", ignore = true)
    CartViewDTO toCartDTO(Cart cart);

    Cart toCart(CartBaseDTO cartDTO);
}
