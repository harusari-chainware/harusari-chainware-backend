package com.harusari.chainware.purchase.query.controller;



import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.purchase.query.dto.PurchaseOrderSearchCondition;
import com.harusari.chainware.purchase.query.dto.PurchaseOrderDetailResponse;
import com.harusari.chainware.purchase.query.dto.PurchaseOrderSummaryResponse;
import com.harusari.chainware.purchase.query.service.PurchaseOrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import com.harusari.chainware.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/purchases")
@RequiredArgsConstructor
@Tag(name = "발주 Query API", description = "발주 목록, 상세 조회 API")
public class PurchaseOrderQueryController {

    private final PurchaseOrderQueryService purchaseOrderQueryService;

    @GetMapping
    @Operation(summary = "발주 목록 조회", description = "로그인한 사용자의 ID 기준으로 발주 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "발주 목록 조회 성공")
    })
    public ResponseEntity<ApiResponse<List<PurchaseOrderSummaryResponse>>> getPurchaseOrders(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute PurchaseOrderSearchCondition condition
    ) {
        Long memberId = userDetails.getMemberId();
        List<PurchaseOrderSummaryResponse> result = purchaseOrderQueryService.getPurchaseOrders(memberId, condition);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(result));
    }

    @GetMapping("/{purchaseOrderId}")
    @Operation(summary = "발주 상세 조회", description = "로그인한 사용자의 ID와 발주 ID 기준으로 상세 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "발주 상세 조회 성공")
    })
    public ResponseEntity<ApiResponse<PurchaseOrderDetailResponse>> getDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long purchaseOrderId
    ) {
        PurchaseOrderDetailResponse detail = purchaseOrderQueryService.getPurchaseOrderDetail(userDetails.getMemberId(), purchaseOrderId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(detail));
    }

}