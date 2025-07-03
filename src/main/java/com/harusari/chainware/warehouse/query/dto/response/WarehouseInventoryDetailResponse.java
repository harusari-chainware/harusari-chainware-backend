package com.harusari.chainware.warehouse.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class WarehouseInventoryDetailResponse {
    private WarehouseSimpleInfo warehouse;
    private ProductSimpleInfo product;
    private InventorySimpleInfo inventory;
    private List<InboundHistory> inboundHistories;
    private List<OutboundHistory> outboundHistories;
}