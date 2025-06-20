package com.harusari.chainware.warehouse.command.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WarehouseInventoryCommandResponse {
    private Long inventoryId;
    private Long productId;
    private Long contractId;
    private int quantity;
}