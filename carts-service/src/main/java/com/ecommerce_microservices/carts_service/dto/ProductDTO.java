package com.ecommerce_microservices.carts_service.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Long id;
    private Long code;
    private String name;
    private String brand;
    private Double price;
    private Integer stock;

}
