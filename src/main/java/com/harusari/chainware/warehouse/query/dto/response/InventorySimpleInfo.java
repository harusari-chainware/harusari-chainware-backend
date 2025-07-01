package com.harusari.chainware.warehouse.query.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class InventorySimpleInfo {
    private Integer quantity;
    private Integer reservedQuantity;

    @QueryProjection
    public InventorySimpleInfo(Integer quantity, Integer reservedQuantity) {
        this.quantity = quantity;
        this.reservedQuantity = reservedQuantity;
    }
}
