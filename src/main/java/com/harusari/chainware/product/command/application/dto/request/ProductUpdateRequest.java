package com.harusari.chainware.product.command.application.dto.request;

import com.harusari.chainware.product.command.domain.aggregate.StoreType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductUpdateRequest {

    private String productName;

    private Integer basePrice;

    private String unitQuantity;

    private String unitSpec;

    private StoreType storeType;

    private Integer safetyStock;

    private String origin;

    private Integer shelfLife;

    private Boolean productStatus = true;

}