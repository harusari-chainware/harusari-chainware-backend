package com.harusari.chainware.delivery.query.dto.response;

import lombok.Getter;
import lombok.Builder;

import java.util.List;

@Getter
@Builder
public class DeliveryDetailResponse {
    private DeliveryDetailInfo deliveryInfo;
    private WarehouseInfo warehouseInfo;
    private FranchiseInfo franchiseInfo;
    private DeliveryOrderInfo orderInfo;
    private List<DeliveryProductInfo> products;
}
