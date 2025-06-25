package com.harusari.chainware.category.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class TopCategoryResponse{
        private Long topCategoryId;
        private String topCategoryName;
        private List<CategoryResponse> categories;
}