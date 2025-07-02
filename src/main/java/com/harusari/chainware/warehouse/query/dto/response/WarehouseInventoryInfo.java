package com.harusari.chainware.warehouse.query.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WarehouseInventoryInfo {

    private Long warehouseInventoryId;
    private Long warehouseId;
    private String warehouseName;
    private Long productId;
    private String productCode;
    private String productName;
    private String unitQuantity;
    private String unitSpec;
    private Integer quantity;
    private Integer reservedQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @QueryProjection
    public WarehouseInventoryInfo(
            Long warehouseInventoryId, Long warehouseId, String warehouseName,
            Long productId, String productCode, String productName,
            String unitQuantity, String unitSpec,
            Integer quantity, Integer reservedQuantity,
            LocalDateTime createdAt, LocalDateTime modifiedAt
    ) {
        this.warehouseInventoryId = warehouseInventoryId;
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.productId = productId;
        this.productCode = productCode;
        this.productName = productName;
        this.unitQuantity = unitQuantity;
        this.unitSpec = unitSpec;
        this.quantity = quantity;
        this.reservedQuantity = reservedQuantity;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

}