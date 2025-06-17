package com.harusari.chainware.vendor.query.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VendorDetailResponse {
    private final VendorDetailDto vendor;
}
