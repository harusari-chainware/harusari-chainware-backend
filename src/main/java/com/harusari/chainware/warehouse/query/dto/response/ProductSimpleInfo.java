package com.harusari.chainware.warehouse.query.dto.response;

import com.harusari.chainware.product.command.domain.aggregate.StoreType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductSimpleInfo {
    private Long productId;
    private String productCode;
    private String productName;
    private String topCategoryName;
    private String categoryName;
    private Integer basePrice;
    private StoreType storeType;
}
