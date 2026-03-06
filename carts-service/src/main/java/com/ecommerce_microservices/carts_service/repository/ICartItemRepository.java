package com.ecommerce_microservices.carts_service.repository;

import com.ecommerce_microservices.carts_service.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICartItemRepository extends JpaRepository<CartItem, Long> {

    void deleteByCartId(Long cartId);
}
