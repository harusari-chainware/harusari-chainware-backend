package com.harusari.chainware.category.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponse {
    private Long categoryId;
    private Long topCategoryId;
    private String categoryName;
    private int productCount;
}