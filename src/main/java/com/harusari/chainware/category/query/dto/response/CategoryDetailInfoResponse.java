package com.harusari.chainware.category.query.dto.response;

import com.harusari.chainware.common.dto.Pagination;
import com.harusari.chainware.product.query.dto.response.ProductDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CategoryDetailInfoResponse {
    private Long categoryId;
    private String categoryName;
    private LocalDateTime categoryCreatedAt;
    private LocalDateTime categoryModifiedAt;
    private Long topCategoryId;
    private String topCategoryName;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<ProductDto> products;
    private Pagination pagination;
}