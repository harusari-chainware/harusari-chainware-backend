package com.harusari.chainware.purchase.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CancelPurchaseOrderRequest {

    @NotBlank(message = "취소 사유는 필수입니다.")
    private String cancelReason;
}