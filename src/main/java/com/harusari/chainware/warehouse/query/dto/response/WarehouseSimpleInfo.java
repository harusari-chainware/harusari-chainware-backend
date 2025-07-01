package com.harusari.chainware.warehouse.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WarehouseSimpleInfo {
    private Long warehouseId;
    private String warehouseName;
    private String warehouseAddress;
    private boolean warehouseStatus;
}
