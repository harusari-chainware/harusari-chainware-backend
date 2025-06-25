package com.harusari.chainware.category.command.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryCommandResponse {

    private final Long categoryId;
    private final Long topCategoryId;
    private final String categoryName;
    private final String categoryCode;
}