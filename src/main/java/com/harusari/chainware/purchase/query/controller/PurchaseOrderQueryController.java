package com.harusari.chainware.purchase.query.controller;



import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.purchase.query.dto.PurchaseOrderSearchCondition;
import com.harusari.chainware.purchase.query.dto.PurchaseOrderDetailResponse;
import com.harusari.chainware.purchase.query.dto.PurchaseOrderSummaryResponse;
import com.harusari.chainware.purchase.query.service.PurchaseOrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseOrderQueryController {

    private final PurchaseOrderQueryService purchaseOrderQueryService;

    @Operation(summary = "발주 목록 조회", description = "로그인한 사용자의 ID 기준으로 발주 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public List<PurchaseOrderSummaryResponse> getPurchaseOrders(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute PurchaseOrderSearchCondition condition
    ) {
        Long memberId = userDetails.getMemberId();
        return purchaseOrderQueryService.getPurchaseOrders(memberId, condition);
    }

    @Operation(summary = "발주 상세 조회", description = "로그인한 사용자의 ID와 발주 ID 기준으로 상세 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/{purchaseOrderId}")
    public PurchaseOrderDetailResponse getDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long purchaseOrderId
    ) {
        return purchaseOrderQueryService.getPurchaseOrderDetail(userDetails.getMemberId(), purchaseOrderId);
    }
}