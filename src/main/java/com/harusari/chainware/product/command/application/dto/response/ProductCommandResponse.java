package com.harusari.chainware.product.command.application.dto.response;

import com.harusari.chainware.product.command.domain.aggregate.StoreType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductCommandResponse {

    private final Long productId;

    private final Long categoryId;

    private final String productName;

    private final String productCode;

    private final String unitQuantity;

    private final String unitSpec;

    private final Integer basePrice;

    private final StoreType storeType;

    private final Integer safetyStock;

    private final String origin;

    private final Integer shelfLife;

    private final Boolean productStatus;
}