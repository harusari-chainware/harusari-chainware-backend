package com.harusari.chainware.category.query.dto.response;

import com.harusari.chainware.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TopCategoryListResponse {
    private List<TopCategoryResponse> topCategories;
    private Pagination pagination;
}