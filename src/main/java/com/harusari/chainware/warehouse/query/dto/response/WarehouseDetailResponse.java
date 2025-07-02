package com.harusari.chainware.warehouse.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class WarehouseDetailResponse {
    private WarehouseBasicInfo warehouseInfo;
    private List<InboundHistoryInfo> inboundHistory;
    private List<OutboundHistoryInfo> outboundHistory;
}