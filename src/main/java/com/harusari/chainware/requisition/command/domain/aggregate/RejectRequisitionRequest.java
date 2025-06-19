package com.harusari.chainware.requisition.command.domain.aggregate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RejectRequisitionRequest {

    @NotBlank(message = "반려 사유는 필수입니다.")
    @Size(max = 255, message = "반려 사유는 최대 255자까지 입력할 수 있습니다.")
    private String rejectReason;
}
