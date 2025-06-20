package com.harusari.chainware.warehouse.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseInventorySnapshotResponseDto {
    private Long warehouseId;
    private String warehouseName;
    private Long productId;
    private String productName;
    private Integer quantity;
}
