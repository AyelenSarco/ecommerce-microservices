package com.ecommerce_microservices.sales_service.model;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Snapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private String productName;
    private double unitPrice;
    private int quantity;
    private double subtotal;

    @ManyToOne
    @JoinColumn(name="saleId", nullable = false)
    private Sale sale;
}
