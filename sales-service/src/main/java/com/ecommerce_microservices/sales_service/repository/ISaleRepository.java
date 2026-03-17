package com.ecommerce_microservices.sales_service.repository;

import com.ecommerce_microservices.sales_service.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISaleRepository extends JpaRepository<Sale, Long> {
}
