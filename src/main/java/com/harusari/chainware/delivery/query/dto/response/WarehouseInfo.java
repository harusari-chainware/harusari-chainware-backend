package com.harusari.chainware.delivery.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WarehouseInfo {
    private String warehouseName;
    private String warehouseAddress;
    private String warehouseManagerName;
}
