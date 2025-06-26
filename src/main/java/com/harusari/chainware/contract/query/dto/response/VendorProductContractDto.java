package com.harusari.chainware.contract.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorProductContractDto {
    private Long vendorId;

    private Long productId;
    private String productName;
    private Long topCategoryId;
    private String topCategoryName;
    private Long categoryId;
    private String categoryName;
    private String storeType;
    private Integer basePrice;
    private String unitQuantity;
    private String unitSpec;

    private Long contractId;
    private Integer contractPrice;
    private Integer minOrderQty;
    private Integer leadTime;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private String contractStatus;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}