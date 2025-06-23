package com.harusari.chainware.warehouse.command.application.dto.request;

import lombok.Getter;

@Getter
public class WarehouseUpdateRequest {
    private String warehouseName;
    private String warehouseAddress;
    private boolean warehouseStatus;
}
