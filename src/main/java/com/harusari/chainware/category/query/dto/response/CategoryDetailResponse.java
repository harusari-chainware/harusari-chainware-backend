package com.harusari.chainware.category.query.dto.response;

import com.harusari.chainware.product.query.dto.response.ProductDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CategoryDetailResponse {
    private Long categoryId;
    private String categoryName;
    private Long topCategoryId;
    private String topCategoryName;
    private List<ProductDto> products = new ArrayList<>();

    public CategoryDetailResponse(Long categoryId, String categoryName, Long topCategoryId, String topCategoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.topCategoryId = topCategoryId;
        this.topCategoryName = topCategoryName;
    }
}