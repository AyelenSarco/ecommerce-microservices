package com.ecommerce_microservices.sales_service.dto.sale;


import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SnapshotDTO {

    private Long id;
    private Long productId;
    private String productName;
    private double unitPrice;
    private int quantity;
    private double subtotal;
}
