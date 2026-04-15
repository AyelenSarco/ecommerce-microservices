package com.ecommerce_microservices.sales_service.repository;

import com.ecommerce_microservices.sales_service.model.Snapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISnapshotRepository extends JpaRepository<Snapshot, Long> {
}
