package com.harusari.chainware.category.query.dto.response;

import com.harusari.chainware.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class TopCategoryProductPageResponse {
    private Long topCategoryId;
    private String topCategoryName;
    private List<CategoryWithProductsResponse> categories;
    private Pagination pagination;
}