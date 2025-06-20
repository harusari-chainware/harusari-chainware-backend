package com.harusari.chainware.warehouse.command.application.dto;

import lombok.Getter;

@Getter
public class CurrentWarehouseInventoryDto {

    private Long warehouseId;
    private Long productId;
    private Integer quantity;
}
