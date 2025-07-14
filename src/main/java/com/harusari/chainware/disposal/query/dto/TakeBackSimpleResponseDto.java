package com.harusari.chainware.disposal.query.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TakeBackSimpleResponseDto {
    private Long takeBackId;
    private String takeBackCode;
    private String createdDate;
    private String franchiseName;
}
