package com.harusari.chainware.contract.query.dto.response;

import com.harusari.chainware.contract.command.domain.aggregate.ContractStatus;
import com.harusari.chainware.product.command.domain.aggregate.StoreType;
import com.harusari.chainware.vendor.command.domain.aggregate.VendorStatus;
import com.harusari.chainware.vendor.command.domain.aggregate.VendorType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class VendorProductContractDto {
    private Long contractId;
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
    private String vendorTaxId;
    private VendorStatus vendorStatus;
    private VendorType vendorType;
    private Long productId;
    private String unitQuantity;
    private String unitSpec;
    private StoreType storeType;
    private Long topCategoryId;
    private String topCategoryName;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}