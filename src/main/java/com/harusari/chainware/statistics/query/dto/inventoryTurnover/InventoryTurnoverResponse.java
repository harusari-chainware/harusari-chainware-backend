package com.harusari.chainware.statistics.query.dto.inventoryTurnover;

import lombok.Getter;

@Getter
public class InventoryTurnoverResponse {

    private Long productId;
    private String productName;
    private Integer totalOutboundQty;
    private Integer averageInventory;
    private Double turnoverRate;

}
