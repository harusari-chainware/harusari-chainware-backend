package com.harusari.chainware.purchase.command.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class PurchaseOrderItemRequest {

    @NotNull(message = "제품 ID는 필수입니다.")
    private Long productId;

    @Positive(message = "수량은 1 이상이어야 합니다.")
    private int quantity;

    @NotNull(message = "단가는 필수입니다.")
    private Integer unitPrice;
}
