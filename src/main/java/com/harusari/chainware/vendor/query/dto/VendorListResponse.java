package com.harusari.chainware.vendor.query.dto;

import com.harusari.chainware.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class VendorListResponse {
    private final List<VendorListDto> vendors;
    private final Pagination pagination;
}
