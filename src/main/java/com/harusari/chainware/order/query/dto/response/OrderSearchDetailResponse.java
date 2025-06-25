package com.harusari.chainware.order.query.dto.response;

import lombok.Getter;
import lombok.Builder;

import java.util.List;

@Getter
@Builder
public class OrderSearchDetailResponse {
    private OrderBasicInfo orderInfo;
    private FranchiseOwnerInfo franchiseOwnerInfo;
    private String rejectReason;
    private List<OrderProductInfo> products;
    private List<DeliveryHistoryInfo> deliveryHistory;
}
