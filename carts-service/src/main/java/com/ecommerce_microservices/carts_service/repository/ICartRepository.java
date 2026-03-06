package com.ecommerce_microservices.carts_service.repository;

import com.ecommerce_microservices.carts_service.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Long> {
}
