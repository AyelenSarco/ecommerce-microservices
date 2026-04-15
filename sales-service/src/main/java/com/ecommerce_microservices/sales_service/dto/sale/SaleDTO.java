package com.ecommerce_microservices.sales_service.dto.sale;


import lombok.*;

import java.util.Date;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleDTO {

    private Long id;
    private Date date;
    private Double total;
    private List<SnapshotDTO> snapshots;
}
