package com.ecommerce_microservices.carts_service.mapper;

import com.ecommerce_microservices.carts_service.dto.ProductDTO;
import com.ecommerce_microservices.carts_service.dto.cartItem.CartItemDTO;
import com.ecommerce_microservices.carts_service.dto.cartItem.CartItemViewDTO;
import com.ecommerce_microservices.carts_service.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {


    @Mapping(target="productName", source="product.name")
    @Mapping(target="productBrand", source="product.brand")
    @Mapping(target="id", source="cartItem.id" )
    CartItemViewDTO toCartItemViewDTO(CartItem cartItem, ProductDTO product);

    CartItem toCartItem(CartItemDTO cartItemDTO);
}
