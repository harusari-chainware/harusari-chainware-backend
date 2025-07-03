package com.harusari.chainware.category.query.dto.response;

import com.harusari.chainware.category.command.domain.aggregate.TopCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TopCategoryDto {
    private Long topCategoryId;
    private String topCategoryName;

    public static TopCategoryDto fromEntity(TopCategory entity) {
        return new TopCategoryDto(entity.getTopCategoryId(), entity.getTopCategoryName());
    }
}