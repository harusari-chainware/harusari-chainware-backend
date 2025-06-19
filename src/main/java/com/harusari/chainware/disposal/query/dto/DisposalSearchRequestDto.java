package com.harusari.chainware.disposal.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisposalSearchRequestDto {
    private Integer page = 1;
    private Integer size = 10;

    private String franchiseName;
    private String warehouseName;
    private String productName;
    private String topCategoryName;
    private String categoryName;
    private String disposalType; // 예: "NORMAL"(창고 폐기), "RETURN"(반품 폐기)

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }
}
