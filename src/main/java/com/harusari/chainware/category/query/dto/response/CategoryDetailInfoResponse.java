package com.harusari.chainware.category.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryDetailInfoResponse {
    private Long categoryId;
    private String categoryName;
    private Long topCategoryId;
    private String topCategoryName;
}