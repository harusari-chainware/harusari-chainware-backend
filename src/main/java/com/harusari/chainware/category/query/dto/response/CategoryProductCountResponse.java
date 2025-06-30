package com.harusari.chainware.category.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryProductCountResponse {
    private Long categoryId;
    private long productCount;
}