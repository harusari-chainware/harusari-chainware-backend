package com.harusari.chainware.warehouse.query.dto.request;

import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public class WarehouseInventorySearchRequest {
    private String warehouseName;
    private String warehouseAddress;
    private Boolean warehouseStatus;
    private String productCode;
    private String productName;
    private Long topCategoryId;
    private Long categoryId;
    private int page;
    private int size;
    private Sort sort;
}