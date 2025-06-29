package com.harusari.chainware.category.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TopCategoryOnlyWithCountResponse {
    private Long topCategoryId;
    private String topCategoryName;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private long productCount;
}