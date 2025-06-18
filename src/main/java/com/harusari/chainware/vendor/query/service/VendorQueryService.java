package com.harusari.chainware.vendor.query.service;

import com.harusari.chainware.vendor.query.dto.VendorDetailResponse;
import com.harusari.chainware.vendor.query.dto.VendorListResponse;
import com.harusari.chainware.vendor.query.dto.VendorSearchRequestDto;

public interface VendorQueryService {

    VendorListResponse getVendors(VendorSearchRequestDto request);

    VendorDetailResponse getVendorDetail(Long vendorId);
}