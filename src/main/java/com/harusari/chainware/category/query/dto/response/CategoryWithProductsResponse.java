package com.harusari.chainware.category.query.dto.response;

import com.harusari.chainware.product.query.dto.response.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CategoryWithProductsResponse {
    private Long categoryId;
    private String categoryName;
    private Long topCategoryId;
    private List<ProductDto> products;

    public CategoryWithProductsResponse(Long categoryId, String categoryName, Long topCategoryId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.topCategoryId = topCategoryId;
    }
}