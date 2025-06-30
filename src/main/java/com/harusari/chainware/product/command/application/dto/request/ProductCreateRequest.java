package com.harusari.chainware.product.command.application.dto.request;

import com.harusari.chainware.product.command.domain.aggregate.StoreType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ProductCreateRequest {

    @NotBlank(message = "제품명은 필수입니다.")
    private final String productName;

    @NotNull(message = "카테고리 ID는 필수입니다.")
    @Min(0)
    private final Long categoryId;

    @NotBlank(message = "단위 수량은 필수입니다.")
    private final String unitQuantity;

    @NotBlank(message = "단위 규격은 필수입니다.")
    private final String unitSpec;

    @NotNull(message = "기본 단가는 필수입니다.")
    @Min(0)
    private final Integer basePrice;

    @NotNull(message = "보관 상태는 필수입니다.")
    private final StoreType storeType;

    @NotNull(message = "안전 재고는 필수입니다.")
    @Min(0)
    private final Integer safetyStock;

    @NotBlank(message = "원산지는 필수입니다.")
    private final String origin;

    @NotNull(message = "유통기한은 필수입니다.")
    @Min(0)
    private final Integer shelfLife;

    @NotBlank(message = "카테고리 코드는 필수입니다.")
    private final String categoryCode;

}