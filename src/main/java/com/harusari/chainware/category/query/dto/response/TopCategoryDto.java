package com.harusari.chainware.category.query.dto.response;

import com.harusari.chainware.category.command.domain.aggregate.TopCategory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TopCategoryDto {
    private Long topCategoryId;
    private String topCategoryName;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static TopCategoryDto fromEntity(TopCategory entity) {
        return new TopCategoryDto(entity.getTopCategoryId(), entity.getTopCategoryName(), entity.getCreatedAt(), entity.getModifiedAt());
    }
}