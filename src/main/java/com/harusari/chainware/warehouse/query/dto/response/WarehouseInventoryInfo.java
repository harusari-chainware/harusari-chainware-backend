package com.harusari.chainware.warehouse.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
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
}