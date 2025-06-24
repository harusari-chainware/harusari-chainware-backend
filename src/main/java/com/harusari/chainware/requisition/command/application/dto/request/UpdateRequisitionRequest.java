package com.harusari.chainware.requisition.command.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdateRequisitionRequest {

    @NotNull(message = "결재자 ID는 필수입니다.")
    private Long approvedMemberId;

    @NotNull(message = "품목 종류 개수는 필수입니다.")
    private Integer productCount;

    @NotNull(message = "총 수량은 필수입니다.")
    private Integer totalQuantity;

    @NotNull(message = "총 금액은 필수입니다.")
    private Long totalPrice;

    @Size(min = 1, message = "최소 1개 이상의 품목이 필요합니다.")
    private List<RequisitionItemRequest> items;
}