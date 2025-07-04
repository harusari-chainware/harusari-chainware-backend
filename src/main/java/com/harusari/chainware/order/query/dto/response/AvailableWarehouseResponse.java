package com.harusari.chainware.order.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AvailableWarehouseResponse {
    private Long warehouseId;
    private String warehouseName;
    private List<RiskyProductInfo> riskyProducts;
}
