package com.harusari.chainware.product.command.application.dto.request;

import com.harusari.chainware.product.command.domain.aggregate.StoreType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductCreateRequest {

    @NotBlank
    private final String productName;

    @NotNull
    @Min(0)
    private final Long categoryId;

    @NotBlank
    private final String unitQuantity;

    @NotBlank
    private final String unitSpec;

    @NotNull
    @Min(0)
    private final Integer basePrice;

    @NotNull
    private final StoreType storeType;

    @NotNull
    @Min(0)
    private final Integer safetyStock;

    @NotBlank
    private final String origin;

    @Min(0)
    private final Integer shelfLife;

    private final String categoryCode;

}