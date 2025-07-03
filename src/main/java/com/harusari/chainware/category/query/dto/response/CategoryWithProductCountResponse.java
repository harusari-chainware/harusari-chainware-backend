package com.harusari.chainware.category.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CategoryWithProductCountResponse {
    private Long categoryId;
    private String categoryName;
    private long productCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}