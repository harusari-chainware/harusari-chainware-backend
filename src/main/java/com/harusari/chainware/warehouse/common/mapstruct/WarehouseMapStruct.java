package com.harusari.chainware.warehouse.common.mapstruct;

import com.harusari.chainware.common.domain.vo.Address;
import com.harusari.chainware.common.dto.AddressRequest;
import com.harusari.chainware.member.command.application.dto.request.warehouse.WarehouseCreateRequest;
import com.harusari.chainware.warehouse.command.domain.aggregate.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedSourcePolicy =  ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface WarehouseMapStruct {

    @Mapping(
            target = "warehouseAddress",
            source = "warehouseCreateRequest.addressRequest",
            qualifiedByName = "addressRequestToAddress"
    )
    Warehouse toWarehouse(WarehouseCreateRequest warehouseCreateRequest, Long memberId);

    @Named("addressRequestToAddress")
    default Address addressRequestToAddress(AddressRequest request) {
        if (request == null) {
            return null;
        }
        return Address.builder()
                .zipcode(request.zipcode())
                .addressRoad(request.addressRoad())
                .addressDetail(request.addressDetail())
                .build();
    }

}