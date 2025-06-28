package com.harusari.chainware.contract.query.dto.request;

import com.harusari.chainware.contract.command.domain.aggregate.Contract;
import com.harusari.chainware.contract.command.domain.aggregate.ContractStatus;
import com.harusari.chainware.vendor.command.domain.aggregate.VendorType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class VendorProductContractSearchRequest {
    private String productName;
    private String topCategoryName;
    private String categoryName;
    private String vendorName;
    private VendorType vendorType;
    private ContractStatus contractStatus;
    private final LocalDate contractStartDate;
    private final LocalDate contractEndDate;

    private Long vendorId;

    private int page = 1;
    private int size = 10;

    public int getOffset() {
        return (page - 1) * size;
    }
}
