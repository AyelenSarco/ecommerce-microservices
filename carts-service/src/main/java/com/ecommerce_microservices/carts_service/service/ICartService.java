package com.ecommerce_microservices.carts_service.service;

import com.ecommerce_microservices.carts_service.dto.cart.CartBaseDTO;
import com.ecommerce_microservices.carts_service.dto.cart.CartViewDTO;
import com.ecommerce_microservices.carts_service.dto.cartItem.CartItemDTO;


public interface ICartService {

    public CartViewDTO createCart();
    public void addItemToCart(Long cartId, CartItemDTO cartItemDTO);
    public void removeItemFromCart(Long cartId, Long  itemId);
    public void emptyCart(Long cartId);
    public CartViewDTO getCart(Long cartId);
    public void closeCart(Long cartId);
}
