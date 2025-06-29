package com.harusari.chainware.category.query.dto.response;

import com.harusari.chainware.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class TopCategoryProductPageResponse {
    private Long topCategoryId;
    private String topCategoryName;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private long productCount;
    private List<CategoryWithProductsResponse> categories;
    private Pagination pagination;
}