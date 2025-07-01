package com.harusari.chainware.warehouse.query.dto.response;

import com.harusari.chainware.product.command.domain.aggregate.StoreType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ProductSimpleInfo {
    private Long productId;
    private String productCode;
    private String productName;
    private String topCategoryName;
    private String categoryName;
    private Integer basePrice;
    private StoreType storeType;

    @QueryProjection
    public ProductSimpleInfo(Long productId, String productCode, String productName,
                             String topCategoryName, String categoryName,
                             Integer basePrice, StoreType storeType) {
        this.productId = productId;
        this.productCode = productCode;
        this.productName = productName;
        this.topCategoryName = topCategoryName;
        this.categoryName = categoryName;
        this.basePrice = basePrice;
        this.storeType = storeType;
    }
}
