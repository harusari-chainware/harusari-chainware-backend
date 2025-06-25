package com.harusari.chainware.category.query.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryByTopCategoryRequest {
    private Long topCategoryId;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }
}