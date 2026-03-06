package com.ecommerce_microservices.carts_service.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private int quantity;
    private double unitPrice;
    private double subtotal;

    @ManyToOne
    @JoinColumn(name = "cartId", nullable = false)
    private Cart cart;

}
