package com.harusari.chainware.category.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryUpdateRequest {

    @NotBlank(message = "카테고리명은 필수입니다.")
    private final String categoryName;

    @NotNull(message = "상위 카테고리Id 선택은 필수입니다.")
    private final Long topCategoryId;

    @NotBlank(message = "카테고리 코드는 필수입니다.")
    private final String categoryCode;
}
