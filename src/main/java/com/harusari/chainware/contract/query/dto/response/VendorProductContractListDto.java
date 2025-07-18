package com.harusari.chainware.contract.query.dto.response;

import com.harusari.chainware.contract.command.domain.aggregate.ContractStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class VendorProductContractListDto {
    private Long   contractId;
    private String vendorName;
    private String productName;
    private Integer basePrice;
    private Integer contractPrice;
    private Integer minOrderQty;
    private Integer leadTime;
    private ContractStatus contractStatus;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private Long vendorId;
}