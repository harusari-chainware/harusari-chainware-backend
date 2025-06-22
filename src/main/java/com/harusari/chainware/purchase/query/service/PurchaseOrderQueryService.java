package com.harusari.chainware.purchase.query.service;

import com.harusari.chainware.purchase.query.dto.request.PurchaseOrderSearchCondition;
import com.harusari.chainware.purchase.query.dto.response.PurchaseOrderDetailResponse;
import com.harusari.chainware.purchase.query.dto.response.PurchaseOrderSummaryResponse;

import java.util.List;

public interface PurchaseOrderQueryService {

    PurchaseOrderDetailResponse getPurchaseOrderDetail(Long purchaseOrderId);

    List<PurchaseOrderSummaryResponse> getPurchaseOrders(PurchaseOrderSearchCondition condition);
}