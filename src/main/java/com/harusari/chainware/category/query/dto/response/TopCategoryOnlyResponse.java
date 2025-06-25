package com.harusari.chainware.category.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TopCategoryOnlyResponse{
    private Long topCategoryId;
    private String topCategoryName;
}