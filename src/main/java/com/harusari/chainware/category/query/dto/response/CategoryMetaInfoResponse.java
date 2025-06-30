package com.harusari.chainware.category.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CategoryMetaInfoResponse {
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
