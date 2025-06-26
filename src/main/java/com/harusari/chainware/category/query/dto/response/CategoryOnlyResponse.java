package com.harusari.chainware.category.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryOnlyResponse {
    private Long categoryId;
    private String categoryName;
}