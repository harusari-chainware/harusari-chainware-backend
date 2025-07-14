package com.harusari.chainware.purchase.query.mapper;

import com.harusari.chainware.purchase.query.dto.PurchaseOrderSearchCondition;
import com.harusari.chainware.purchase.query.dto.PurchaseOrderDetailResponse;
import com.harusari.chainware.purchase.query.dto.PurchaseOrderProductResponse;
import com.harusari.chainware.purchase.query.dto.PurchaseOrderSummaryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PurchaseOrderQueryMapper {

    PurchaseOrderDetailResponse findPurchaseOrderById(Long memberId, @Param("purchaseOrderId") Long purchaseOrderId);

    List<PurchaseOrderProductResponse> findProductsByPurchaseOrderId(@Param("purchaseOrderId") Long memberId, Long purchaseOrderId);

    List<PurchaseOrderSummaryResponse> findPurchaseOrders(@Param("condition") Long memberId, PurchaseOrderSearchCondition condition);
}