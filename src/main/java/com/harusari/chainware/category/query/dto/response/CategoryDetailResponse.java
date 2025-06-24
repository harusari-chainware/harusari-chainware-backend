package com.harusari.chainware.category.query.dto.response;

import com.harusari.chainware.product.query.dto.response.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CategoryDetailResponse {
    private Long categoryId;
    private String categoryName;
    private Long topCategoryId;
    private String topCategoryName;
    private List<ProductDto> products;
}