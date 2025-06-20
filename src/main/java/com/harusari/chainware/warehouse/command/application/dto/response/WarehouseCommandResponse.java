package com.harusari.chainware.warehouse.command.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WarehouseCommandResponse {
    private Long warehouseId;
    private String warehouseName;
    private String warehouseAddress;
    private boolean warehouseStatus;
}
