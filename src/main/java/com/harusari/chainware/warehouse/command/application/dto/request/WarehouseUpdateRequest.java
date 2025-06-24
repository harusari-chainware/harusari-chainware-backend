package com.harusari.chainware.warehouse.command.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WarehouseUpdateRequest {
    private String warehouseName;
    private String warehouseAddress;
    private boolean warehouseStatus;
}
