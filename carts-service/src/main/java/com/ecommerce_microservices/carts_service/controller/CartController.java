package com.ecommerce_microservices.carts_service.controller;


import com.ecommerce_microservices.carts_service.dto.ApiResponse;
import com.ecommerce_microservices.carts_service.dto.cart.CartBaseDTO;
import com.ecommerce_microservices.carts_service.dto.cart.CartViewDTO;
import com.ecommerce_microservices.carts_service.dto.cartItem.CartItemDTO;
import com.ecommerce_microservices.carts_service.dto.cartItem.CartItemViewDTO;
import com.ecommerce_microservices.carts_service.model.Cart;
import com.ecommerce_microservices.carts_service.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
public class CartController {
    private CartService cartService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCart() {

        CartViewDTO cartViewDTO = cartService.createCart();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Cart created successfully", cartViewDTO));
    }

    @PatchMapping("/{id}/add/item")
    public ResponseEntity<ApiResponse> addItemToCart(@PathVariable(name = "id") Long cartId,
                                                     @Validated @RequestBody CartItemDTO cartItemDTO) {
        cartService.addItemToCart(cartId, cartItemDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Cart item added successfully", null));

    }

    // It's okey here mapping as a Patch request for removing from a list? O it has to be a Delete Mapping? Above it's okey a Patch?

    @PatchMapping("/{cartId}/delete/item/{itemId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable(name = "cartId") Long cartId,
                                                          @PathVariable(name = "itemId") Long itemId){

        cartService.removeItemFromCart(cartId, itemId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Cart item removed successfully from cart", null));
    }

    // post? delete?

    @PostMapping("/{id}/empty")
    public ResponseEntity<ApiResponse> emptyCart(@PathVariable(name = "id") Long cartId) {
        cartService.emptyCart(cartId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Cart is empty", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CartViewDTO>> getCart(@PathVariable(name = "id") Long cartId) {
        CartViewDTO cartViewDTO = cartService.getCart(cartId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Cart", cartViewDTO));
    }

    @RequestMapping(
            value = "/{id}/close",
            method = RequestMethod.PATCH
    )
    public ResponseEntity<ApiResponse> closeCart(@PathVariable(name = "id") Long id) {
        cartService.closeCart(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Cart closed successfully", null));
    }

    // To test Circuit Breaker and OpenFeing
    @GetMapping("/product/{id}")
    public ResponseEntity<ApiResponse<CartItemViewDTO>> getCartItem(@PathVariable(name = "id") Long productId) {


        CartItemViewDTO itemViewDTO = cartService.getItemView(productId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Item", itemViewDTO));

    }


}
