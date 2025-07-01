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
    private List<InboundHistoryInfo> inboundHistory;     // 입고 이력
    private List<OutboundHistoryInfo> outboundHistory;   // 배송 이력
}