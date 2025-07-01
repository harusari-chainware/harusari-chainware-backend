package com.harusari.chainware.warehouse.query.dto.response;

import com.harusari.chainware.common.domain.vo.Address;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WarehouseBasicInfo {
    private String warehouseName;
    private Address warehouseAddress;
    private boolean warehouseStatus;
    private String managerName;
    private String managerPhone;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @QueryProjection
    public WarehouseBasicInfo(String warehouseName, Address warehouseAddress, boolean warehouseStatus,
                              String managerName, String managerPhone, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.warehouseName = warehouseName;
        this.warehouseAddress = warehouseAddress;
        this.warehouseStatus = warehouseStatus;
        this.managerName = managerName;
        this.managerPhone = managerPhone;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}