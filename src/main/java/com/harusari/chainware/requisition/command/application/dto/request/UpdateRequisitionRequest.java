package com.harusari.chainware.requisition.command.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdateRequisitionRequest {

    @NotNull(message = "결재자 ID는 필수입니다.")
    private Long approvedMemberId;

    @Size(min = 1, message = "최소 1개 이상의 품목이 필요합니다.")
    private List<RequisitionItemRequest> items;
}