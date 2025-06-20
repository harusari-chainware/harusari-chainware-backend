package com.harusari.chainware.category.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TopCategoryUpdateRequest(
        @NotBlank(message = "상위 카테고리명은 필수입니다.") String topCategoryName
) {
}