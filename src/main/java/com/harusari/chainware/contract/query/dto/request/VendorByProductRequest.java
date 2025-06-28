package com.harusari.chainware.contract.query.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VendorByProductRequest {
    private Long productId;
    private Integer page = 1;
    private Integer size = 10;

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }

}