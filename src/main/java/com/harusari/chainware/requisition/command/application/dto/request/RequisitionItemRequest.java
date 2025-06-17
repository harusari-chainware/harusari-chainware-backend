package com.harusari.chainware.requisition.command.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class RequisitionItemRequest {

    @NotNull(message = "계약 ID는 필수입니다.")
    private Long contractId;

    @NotNull(message = "제품 ID는 필수입니다.")
    private Long productId;

    @Positive(message = "수량은 1 이상이어야 합니다.")
    private int quantity;

    @NotNull(message = "단가는 필수입니다.")
    private BigDecimal unitPrice;
}
