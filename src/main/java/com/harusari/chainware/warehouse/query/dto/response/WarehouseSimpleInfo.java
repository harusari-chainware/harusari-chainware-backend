package com.harusari.chainware.warehouse.query.dto.response;

import com.harusari.chainware.common.domain.vo.Address;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class WarehouseSimpleInfo {
    private Long warehouseId;
    private String warehouseName;
    private Address warehouseAddress;
    private boolean warehouseStatus;

    @QueryProjection
    public WarehouseSimpleInfo(Long warehouseId, String warehouseName, Address warehouseAddress, boolean warehouseStatus) {
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.warehouseAddress = warehouseAddress;
        this.warehouseStatus = warehouseStatus;
    }
}
