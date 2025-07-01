package com.harusari.chainware.warehouse.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class WarehouseSearchResponse {
    private Long warehouseId;
    private String warehouseName;
    private String warehouseAddress;
    private String managerName;
    private String managerPhoneNumber;
    private boolean warehouseStatus;
    private LocalDateTime createdAt;
}