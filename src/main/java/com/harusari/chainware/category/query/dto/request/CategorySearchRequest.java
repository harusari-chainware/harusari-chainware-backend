package com.harusari.chainware.category.query.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategorySearchRequest {
    private Long topCategoryId;
    private String topCategoryName;
    private Long categoryId;
    private String categoryName;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;

    private String sortBy;
    private String sortDir;

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }

    public String getSortByOrDefault() {
        return (sortBy == null || sortBy.isBlank()) ? "c.category_id" : mapToColumn(sortBy);
    }

    public String getSortDirOrDefault() {
        return (sortDir != null && sortDir.equalsIgnoreCase("desc")) ? "DESC" : "ASC";
    }

    private String mapToColumn(String field) {
        return switch (field) {
            case "categoryName" -> "c.category_name";
            case "topCategoryName" -> "tc.top_category_name";
            case "productCount" -> "productCount";
            default -> "c.category_id";
        };
    }
}