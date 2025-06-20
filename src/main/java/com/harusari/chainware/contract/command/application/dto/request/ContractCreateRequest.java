package com.harusari.chainware.contract.command.application.dto.request;

import com.harusari.chainware.contract.command.domain.aggregate.ContractStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ContractCreateRequest {

    @NotNull(message = "제품 ID는 필수입니다.")
    private Long productId;

    @NotNull(message = "거래처 ID는 필수입니다.")
    private Long vendorId;

    @NotNull(message = "계약 단가는 필수입니다.")
    @Min(value = 0, message = "계약 단가는 0 이상이어야 합니다.")
    private Integer contractPrice;

    @NotNull(message = "최소 발주 수량은 필수입니다.")
    @Min(value = 1, message = "최소 발주 수량은 1개 이상이어야 합니다.")
    private Integer minOrderQty;

    @NotNull(message = "납기일은 필수입니다.")
    @Min(value = 0, message = "납기일은 0일 이상이어야 합니다.")
    private Integer leadTime;

    @NotNull(message = "계약 시작일은 필수입니다.")
    private LocalDate contractStartDate;

    @NotNull(message = "계약 만료일은 필수입니다.")
    private LocalDate contractEndDate;

    @NotNull(message = "계약 상태는 필수입니다.")
    private ContractStatus contractStatus;

}