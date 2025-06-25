package com.harusari.chainware.category.query.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryByTopCategoryRequest {
    private Long topCategoryId;
}