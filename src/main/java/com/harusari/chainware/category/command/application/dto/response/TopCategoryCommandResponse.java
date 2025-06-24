package com.harusari.chainware.category.command.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TopCategoryCommandResponse {

    private final Long topCategoryId;
    private final String topCategoryName;
}