package com.harusari.chainware.warehouse.query.dto.response;

import com.harusari.chainware.common.domain.vo.Address;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WarehouseSearchResponse {
    private Long warehouseId;
    private String warehouseName;
    private Address warehouseAddress;
    private String managerName;
    private String managerPhoneNumber;
    private boolean warehouseStatus;
    private LocalDateTime createdAt;

    @QueryProjection
    public WarehouseSearchResponse(
            Long warehouseId, String warehouseName, Address warehouseAddress,
            String managerName, String managerPhoneNumber, boolean warehouseStatus, LocalDateTime createdAt
    ) {
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.warehouseAddress = warehouseAddress;
        this.managerName = managerName;
        this.managerPhoneNumber = managerPhoneNumber;
        this.warehouseStatus = warehouseStatus;
        this.createdAt = createdAt;
    }

}