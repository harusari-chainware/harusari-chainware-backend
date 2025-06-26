package com.harusari.chainware.category.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TopCategoryWithCategoriesResponse {
    private Long topCategoryId;
    private String topCategoryName;
    private List<CategoryWithProductCountResponse> categories;
}