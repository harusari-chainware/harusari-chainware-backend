package com.harusari.chainware.order.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AvailableWarehouseResponse {
    private Long warehouseId;
    private String warehouseName;
}
