package com.harusari.chainware.disposal.query.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DisposalProductSearchResponseDto {
    private Long productId;
    private String productName;
    private String productCode;
    private Integer stock;
}
