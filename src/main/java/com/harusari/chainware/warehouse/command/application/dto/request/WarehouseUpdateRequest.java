package com.harusari.chainware.warehouse.command.application.dto.request;

import com.harusari.chainware.common.dto.AddressRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WarehouseUpdateRequest {
    private String warehouseName;
    private AddressRequest warehouseAddress;
    private boolean warehouseStatus;
}
