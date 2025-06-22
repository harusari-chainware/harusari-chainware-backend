package com.harusari.chainware.purchase.query.controller;



import com.harusari.chainware.purchase.query.dto.request.PurchaseOrderSearchCondition;
import com.harusari.chainware.purchase.query.dto.response.PurchaseOrderDetailResponse;
import com.harusari.chainware.purchase.query.dto.response.PurchaseOrderSummaryResponse;
import com.harusari.chainware.purchase.query.service.PurchaseOrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderQueryController {

    private final PurchaseOrderQueryService purchaseOrderQueryService;

    @Operation(summary = "발주 목록 조회", description = "발주 ID 기준으로 상세 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public List<PurchaseOrderSummaryResponse> getPurchaseOrders(PurchaseOrderSearchCondition condition) {
        return purchaseOrderQueryService.getPurchaseOrders(condition);
    }


    @Operation(summary = "발주 상세 조회", description = "발주 ID 기준으로 상세 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/{purchaseOrderId}")
    public PurchaseOrderDetailResponse getDetail(@PathVariable Long purchaseOrderId) {
        return purchaseOrderQueryService.getPurchaseOrderDetail(purchaseOrderId);
    }
}
