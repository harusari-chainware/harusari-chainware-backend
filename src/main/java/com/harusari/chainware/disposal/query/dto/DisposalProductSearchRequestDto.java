package com.harusari.chainware.disposal.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DisposalProductSearchRequestDto {

    @Schema(description = "검색 키워드 (상품명 또는 상품코드)")
    private String keyword;

    @Schema(description = "검색 유형: WAREHOUSE 또는 FRANCHISE")
    private String type;
}
