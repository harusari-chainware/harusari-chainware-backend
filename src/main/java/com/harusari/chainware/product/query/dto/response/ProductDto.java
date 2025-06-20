package com.harusari.chainware.product.query.dto.response;

import com.harusari.chainware.product.command.domain.aggregate.StoreType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDto {
    private Long productId;
    private String productName;
    private String productCode;
    private Long categoryId;
    private Integer basePrice;
    private String unitQuantity;
    private String unitSpec;
    private StoreType storeType;
    private Integer safetyStock;
    private String origin;
    private Integer shelfLife;
    private boolean productStatus;
}