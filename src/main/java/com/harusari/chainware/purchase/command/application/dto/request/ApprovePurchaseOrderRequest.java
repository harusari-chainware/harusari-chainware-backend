package com.harusari.chainware.purchase.command.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ApprovePurchaseOrderRequest {

    @NotNull(message = "발주 ID는 필수입니다.")
    private Long purchaseOrderId;

    public ApprovePurchaseOrderRequest() {
    }

    public ApprovePurchaseOrderRequest(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }
}