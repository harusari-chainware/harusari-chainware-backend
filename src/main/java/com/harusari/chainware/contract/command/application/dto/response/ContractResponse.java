package com.harusari.chainware.contract.command.application.dto.response;

import com.harusari.chainware.contract.command.domain.aggregate.ContractStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ContractResponse {
    private Long contractId;
    private Long productId;
    private Long vendorId;
    private Integer contractPrice;
    private Integer minOrderQty;
    private Integer leadTime;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private ContractStatus contractStatus;
}