package com.harusari.chainware.purchase.query.mapper;

import com.harusari.chainware.purchase.query.dto.request.PurchaseOrderSearchCondition;
import com.harusari.chainware.purchase.query.dto.response.PurchaseOrderDetailResponse;
import com.harusari.chainware.purchase.query.dto.response.PurchaseOrderProductResponse;
import com.harusari.chainware.purchase.query.dto.response.PurchaseOrderSummaryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PurchaseOrderQueryMapper {

    PurchaseOrderDetailResponse findPurchaseOrderById(@Param("purchaseOrderId") Long memberId, Long purchaseOrderId);

    List<PurchaseOrderProductResponse> findProductsByPurchaseOrderId(@Param("purchaseOrderId") Long memberId, Long purchaseOrderId);

    List<PurchaseOrderSummaryResponse> findPurchaseOrders(@Param("condition") Long memberId, PurchaseOrderSearchCondition condition);
}