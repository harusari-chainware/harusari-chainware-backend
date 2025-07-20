package com.harusari.chainware.warehouse.query.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class InventorySimpleInfo {

    private Integer quantity;
    private Integer reservedQuantity;
    private Integer safetyQuantity;

    @QueryProjection
    public InventorySimpleInfo(
            Integer quantity, Integer reservedQuantity, Integer safetyQuantity
    ) {
        this.quantity = quantity;
        this.reservedQuantity = reservedQuantity;
        this.safetyQuantity = safetyQuantity;
    }

}
