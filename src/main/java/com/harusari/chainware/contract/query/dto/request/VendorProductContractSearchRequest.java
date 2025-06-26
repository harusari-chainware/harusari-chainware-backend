package com.harusari.chainware.contract.query.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class VendorProductContractSearchRequest {
    private final Long vendorId;
    @Builder.Default private final Integer page = 1;
    @Builder.Default private final Integer size = 10;

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }
}