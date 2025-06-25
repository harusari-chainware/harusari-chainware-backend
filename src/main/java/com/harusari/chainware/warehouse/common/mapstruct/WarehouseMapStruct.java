package com.harusari.chainware.warehouse.common.mapstruct;

import com.harusari.chainware.member.command.application.dto.request.warehouse.WarehouseCreateRequest;
import com.harusari.chainware.warehouse.command.domain.aggregate.Warehouse;
import org.mapstruct.Mapper;

@Mapper
public interface WarehouseMapStruct {

    Warehouse toWarehouse(WarehouseCreateRequest warehouseCreateRequest, Long memberId);

}