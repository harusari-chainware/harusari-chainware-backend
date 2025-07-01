package com.harusari.chainware.warehouse.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InventorySimpleInfo {
    private Integer quantity;
    private Integer reservedQuantity;
}
