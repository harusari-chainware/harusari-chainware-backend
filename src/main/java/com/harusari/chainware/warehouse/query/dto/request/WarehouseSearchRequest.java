package com.harusari.chainware.warehouse.query.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseSearchRequest {
    private String warehouseName;
    private String warehouseAddress;
    private Boolean warehouseStatus;
}