package com.harusari.chainware.purchase.command.application.controller;


import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.purchase.command.application.dto.request.CancelPurchaseOrderRequest;
import com.harusari.chainware.purchase.command.application.dto.request.RejectPurchaseOrderRequest;
import com.harusari.chainware.purchase.command.application.dto.request.UpdatePurchaseOrderRequest;
import com.harusari.chainware.purchase.command.application.service.PurchaseOrderCommandService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseOrderCommandController {
    private final PurchaseOrderCommandService purchaseOrderCommandService;


    @PutMapping("/{purchaseOrderId}/approve")
    @Operation(summary = "발주 승인", description = "거래처 담당자가 발주를 승인합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "발주서 승인됨")
    public ResponseEntity<ApiResponse<Void>> approvePurchaseOrder(
            @PathVariable("purchaseOrderId") Long purchaseOrderId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        purchaseOrderCommandService.approvePurchaseOrder(purchaseOrderId, userDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }


    @PutMapping("/{purchaseOrderId}/reject")
    @Operation(summary = "발주 거절", description = "거래처 담당자가 발주를 거절합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "발주서 거절됨")
    public ResponseEntity<ApiResponse<Void>> rejectPurchaseOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("purchaseOrderId") Long purchaseOrderId,
            @Valid @RequestBody RejectPurchaseOrderRequest request

    ) {
        purchaseOrderCommandService.rejectPurchaseOrder(userDetails.getMemberId(), purchaseOrderId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }


    @PutMapping("/{purchaseOrderId}/cancel")
    @Operation(summary = "발주 취소", description = "발주 요청을 취소합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "발주서 취소됨")
    public ResponseEntity<ApiResponse<Void>> cancelPurchaseOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("purchaseOrderId") Long purchaseOrderId,
            @RequestBody @Valid CancelPurchaseOrderRequest request
    ) {
        purchaseOrderCommandService.cancelPurchaseOrder(
                userDetails.getMemberId(),
                purchaseOrderId,
                request,
                userDetails.getMemberAuthorityType()
        );
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/{purchaseOrderId}")
    @Operation(summary = "발주서 수정", description = "REQUESTED 상태일 때만 발주서를 수정할 수 있습니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "발주서 승인됨")
    public ResponseEntity<ApiResponse<Void>> updatePurchaseOrder(
            @PathVariable Long purchaseOrderId,
            @RequestBody @Valid UpdatePurchaseOrderRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        purchaseOrderCommandService.updatePurchaseOrder(purchaseOrderId, request, user.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }



}
