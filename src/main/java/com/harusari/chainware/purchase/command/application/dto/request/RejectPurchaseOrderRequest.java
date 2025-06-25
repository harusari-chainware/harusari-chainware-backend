package com.harusari.chainware.purchase.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RejectPurchaseOrderRequest {

    @NotBlank(message = "거절 사유는 필수입니다.")
    @Size(max = 255, message = "거절 사유는 최대 255자까지 입력할 수 있습니다.")
    private String rejectReason;
}
