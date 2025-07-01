package com.harusari.chainware.warehouse.command.application.dto.response;

import com.harusari.chainware.common.domain.vo.Address;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WarehouseCommandResponse {
    private Long warehouseId;
    private String warehouseName;
    private Address warehouseAddress;
    private boolean warehouseStatus;
}
