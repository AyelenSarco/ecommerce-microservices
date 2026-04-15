package com.ecommerce_microservices.sales_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private double total;

    private Long cartId;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<Snapshot> snapshots;
}
