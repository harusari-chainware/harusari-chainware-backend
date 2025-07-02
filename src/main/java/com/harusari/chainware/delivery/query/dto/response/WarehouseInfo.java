package com.harusari.chainware.delivery.query.dto.response;

import com.harusari.chainware.common.domain.vo.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WarehouseInfo {
    private String warehouseName;
    private Address warehouseAddress;
    private String warehouseManagerName;
}
