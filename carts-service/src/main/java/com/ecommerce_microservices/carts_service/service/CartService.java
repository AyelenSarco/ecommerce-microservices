package com.ecommerce_microservices.carts_service.service;


import com.ecommerce_microservices.carts_service.dto.ProductDTO;
import com.ecommerce_microservices.carts_service.dto.cart.CartBaseDTO;
import com.ecommerce_microservices.carts_service.dto.cart.CartViewDTO;
import com.ecommerce_microservices.carts_service.dto.cartItem.CartItemDTO;
import com.ecommerce_microservices.carts_service.dto.cartItem.CartItemViewDTO;
import com.ecommerce_microservices.carts_service.exception.NotFoundException;
import com.ecommerce_microservices.carts_service.mapper.CartItemMapper;
import com.ecommerce_microservices.carts_service.mapper.CartMapper;
import com.ecommerce_microservices.carts_service.model.Cart;
import com.ecommerce_microservices.carts_service.model.CartItem;
import com.ecommerce_microservices.carts_service.model.CartStatusEnum;
import com.ecommerce_microservices.carts_service.repository.ICartItemRepository;
import com.ecommerce_microservices.carts_service.repository.ICartRepository;
import com.ecommerce_microservices.carts_service.repository.ProductServiceClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CartService implements ICartService {

    // Repositories
    private final ICartRepository cartRepository;
    private final ICartItemRepository cartItemRepository;

    // Mappers
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    //Products service client
    private final ProductServiceClient productServiceClient;


    /**
     * Create a new Cart with OPEN status
     * **/
    @Override
    public CartViewDTO createCart() {
        Cart cart = new Cart();

        cartRepository.save(cart);

        return cartMapper.toCartDTO(cart);
    }

    /**
     * This method search the corresponding cart to add the item.
     * If the new cart Item it isn´t in the cart list, it will add the new one consulting the price to the product-service,
     *      to calculate the subtotal for each product and total price of the cart.
     * if not, just add quantity to the existing cartItem and recalculate the total.
     *
     * It's important to save the unitPrice on this moment in order to compare it with the sale moment.
     * If the price change between these operation (we are going to code that validation in sales-service) there will be a message
     * with that information for the user.
     *
     * I chose this option to simplify the system, but in a real case we could pause for a few minutes for the entire cart with the existing prices
     * at the adding operation, after that time, the cart will be empty again.
     * It'll always depend on the business rules.
     * **/

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CircuitBreaker(name="products-service", fallbackMethod = "fallBackGetProduct")
    public void addItemToCart(Long cartId, CartItemDTO newCartItemDTO) {

        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new NotFoundException("Cart not found"));

        CartItem newCartItem = cartItemMapper.toCartItem(newCartItemDTO);


        CartItem cartItemDB = cart.getItems().stream().filter(item -> item.getProductId().equals(newCartItem.getProductId())).findFirst().orElse(null);

        if (   cartItemDB == null ) {
            // to do: productServiceClient.getProductUnityPrice(productId) --> we only need the product price here
            ProductDTO product = productServiceClient.getProductById(newCartItem.getProductId()).getData();

            newCartItem.setUnitPrice(product.getPrice());

            newCartItem.setSubtotal(newCartItem.getUnitPrice() * newCartItem.getQuantity());
            cart.setTotal( cart.getTotal() + newCartItem.getSubtotal());

            newCartItem.setCart(cart);
            cart.getItems().add(newCartItem);

            cartItemRepository.save(newCartItem);
        } else {
            cartItemDB.setQuantity(cartItemDB.getQuantity() + newCartItem.getQuantity());
            cart.setTotal(cart.getTotal() + cartItemDB.getUnitPrice() * newCartItem.getQuantity());

            cartItemRepository.save(cartItemDB);
        }


        cartRepository.save(cart);
    }

    /**
     * This method will:
     * - Validate the existence of the cart and the item.
     * - Remove an item from the cart list.
     * - Recalculate the total amount of the cart
     * - Save the cart updated
     * **/

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeItemFromCart(Long cartId, Long itemId) {

        Cart cart =  cartRepository.findById(cartId).orElseThrow(() -> new NotFoundException("Cart not found"));

        CartItem cartItem = cartItemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Cart Item not found"));

        cartItemRepository.deleteById(cartItem.getId());
        cart.setTotal( cart.getTotal() - cartItem.getSubtotal());

        cartRepository.save(cart);

    }

    /**
     * This method will delete all the cartItems from the cart with id = cartId
     **/

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void emptyCart(Long cartId) {

        if ( ! cartRepository.existsById(cartId)) { throw new NotFoundException("Cart not found"); }

        cartItemRepository.deleteByCartId(cartId);
    }

    /**
     * In order to obtain the entire cart, this method will:
     * - search the specific cart, or throw an exception
     * - map the cart from the DB to a cartViewDTO
     * - for each cartItem:
     *         - request for more information to the products-service (for name and product brand)
     *         - mapping all the necessary information to a cartItemViewDTO.
     *         - Add the cartItemViewDTO to a List
     * - set the above list to the cartViewDTO and return it
     **/
    @Override
    @CircuitBreaker(name="products-service", fallbackMethod = "fallBackGetProduct")
    public CartViewDTO getCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new NotFoundException("Cart not found"));


        CartViewDTO cartViewDTO = cartMapper.toCartDTO(cart);
        List<CartItemViewDTO> itemsViewDTO = new ArrayList<>();


        for (CartItem cartItem : cart.getItems()) {

            // get information about the product from products-service
            ProductDTO productDTO = productServiceClient.getProductById(cartItem.getProductId()).getData();

            // mapping the cart Item with all the information
            CartItemViewDTO cartItemViewDTO = cartItemMapper.toCartItemViewDTO(cartItem, productDTO);

            // adding the cart Item View DTO to de final list
            itemsViewDTO.add(cartItemViewDTO);
        }

        cartViewDTO.setCartItems(itemsViewDTO);

        return cartViewDTO;
    }


    /** This method will change de cart status to CLOSED**/
    @Override
    public void closeCart(Long cartId) {

        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new NotFoundException("Cart not found"));

        cart.setStatus(CartStatusEnum.CLOSED);
        cartRepository.save(cart);

        // if there was user in the system we need to create a new cart for the user here?

    }


    public CartItemViewDTO fallBackGetProduct(Long cartId,Throwable throwable) {

        System.out.println("FALLBACK ACTIVATED");
        throwable.printStackTrace();

        return CartItemViewDTO.builder()
                .id(null)
                .productId(null)
                .quantity(null)
                .productName("xxx")
                .productBrand("xxx")
                .build();
    }



    // This method it's only to test the OpenFeign comunication with product-service and the @CircuitBreaker
    @CircuitBreaker(name="products-service", fallbackMethod = "fallBackGetProduct")
    public CartItemViewDTO getItemView(Long productId){
        ProductDTO productDTO = productServiceClient.getProductById(productId).getData();

        return CartItemViewDTO.builder()
                .id(null)
                .productId(productDTO.getId())
                .productName(productDTO.getName())
                .quantity(3)
                .productBrand(productDTO.getBrand())
                .build();
    }

}
