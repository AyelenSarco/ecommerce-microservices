package com.ecommerce_microservices.products_service.dto;


import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.aspectj.bridge.IMessage;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {


    private Long id;
    private Long code;
    private String name;
    private String brand;
    private Double price;
    private int stock;
}
