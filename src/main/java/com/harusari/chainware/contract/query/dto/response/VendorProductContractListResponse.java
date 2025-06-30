package com.harusari.chainware.contract.query.dto.response;

import com.harusari.chainware.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class VendorProductContractListResponse {
    private List<VendorProductContractDto> contracts;
    private Pagination pagination;
}