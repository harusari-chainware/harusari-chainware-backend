package com.harusari.chainware.category.query.dto.response;

import com.harusari.chainware.product.query.dto.response.ProductDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CategoryWithProductsResponse {
    private Long categoryId;
    private String categoryName;
    private LocalDateTime categoryCreatedAt;
    private LocalDateTime categoryModifiedAt;
    private long productCount;
    private List<ProductDto> products;
}