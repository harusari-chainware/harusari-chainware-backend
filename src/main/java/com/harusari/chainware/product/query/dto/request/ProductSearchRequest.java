package com.harusari.chainware.product.query.dto.request;

import com.harusari.chainware.product.command.domain.aggregate.StoreType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductSearchRequest {
    private String productName;
    private String productCode;
    private Long categoryId;
    private StoreType storeType;
    private String origin;
    private Integer shelfLife;
    private Boolean includeInactive;
    private final ProductStatusFilter productStatusFilter;
}