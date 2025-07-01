package com.harusari.chainware.warehouse.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class WarehouseBasicInfo {
    private String warehouseName;
    private String warehouseAddress;
    private boolean warehouseStatus;
    private String managerName;
    private String managerPhone;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}