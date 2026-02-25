package com.ecommerce_microservices.products_service.repository;

import com.ecommerce_microservices.products_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {

    boolean existByCode(Long code);
}
