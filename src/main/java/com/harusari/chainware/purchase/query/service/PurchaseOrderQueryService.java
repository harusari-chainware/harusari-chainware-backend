package com.harusari.chainware.purchase.query.service;

import com.harusari.chainware.purchase.query.dto.PurchaseOrderListResponse;
import com.harusari.chainware.purchase.query.dto.PurchaseOrderSearchCondition;
import com.harusari.chainware.purchase.query.dto.PurchaseOrderDetailResponse;
import com.harusari.chainware.purchase.query.dto.PurchaseOrderSummaryResponse;

import java.util.List;

public interface PurchaseOrderQueryService {

    PurchaseOrderDetailResponse getPurchaseOrderDetail(Long memberId, Long purchaseOrderId);

    PurchaseOrderListResponse getPurchaseOrders(Long memberId, PurchaseOrderSearchCondition condition); // ✅ 수정
}