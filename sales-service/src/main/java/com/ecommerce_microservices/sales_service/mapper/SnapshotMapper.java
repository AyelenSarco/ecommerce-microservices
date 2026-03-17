package com.ecommerce_microservices.sales_service.mapper;


import com.ecommerce_microservices.sales_service.dto.sale.SnapshotDTO;
import com.ecommerce_microservices.sales_service.model.Snapshot;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SnapshotMapper {

    SnapshotDTO toSnapshotDTO(Snapshot snapshot);

    Snapshot toSnapshot(SnapshotDTO snapshotDTO);
}
