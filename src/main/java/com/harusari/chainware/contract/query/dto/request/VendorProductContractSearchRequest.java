package com.harusari.chainware.contract.query.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendorProductContractSearchRequest {
    private Long vendorId;
    @Builder.Default private Integer page = 1;
    @Builder.Default private Integer size = 10;

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }
}