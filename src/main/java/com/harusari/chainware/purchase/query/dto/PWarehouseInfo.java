package com.harusari.chainware.purchase.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PWarehouseInfo {
    private Long warehouseId;
    private String name;
    private String managerName;
    private String warehouseContact;
}
