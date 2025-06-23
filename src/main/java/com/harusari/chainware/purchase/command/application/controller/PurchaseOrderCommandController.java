package com.harusari.chainware.purchase.command.application.controller;


import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.purchase.command.application.service.PurchaseOrderCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseOrderCommandController {
    private final PurchaseOrderCommandService purchaseOrderCommandService;


    @PutMapping("/{purchaseOrderId}/approve")
    public ResponseEntity<ApiResponse<Void>> approvePurchaseOrder(
            @PathVariable("purchaseOrderId") Long purchaseOrderId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        purchaseOrderCommandService.approve(purchaseOrderId, userDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
