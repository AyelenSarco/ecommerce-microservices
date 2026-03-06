package com.ecommerce_microservices.carts_service.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double total;

    private CartStatusEnum status = CartStatusEnum.OPEN;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> items;
}
