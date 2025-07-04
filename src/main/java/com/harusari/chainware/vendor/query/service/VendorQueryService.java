package com.harusari.chainware.vendor.query.service;

import com.harusari.chainware.vendor.query.dto.VendorPresignedUrlResponse;
import com.harusari.chainware.vendor.query.dto.response.VendorDetailResponse;
import com.harusari.chainware.vendor.query.dto.request.VendorSearchRequest;
import com.harusari.chainware.vendor.query.dto.response.VendorSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VendorQueryService {

    Page<VendorSearchResponse> searchVendors(VendorSearchRequest vendorSearchRequest, Pageable pageable);

    VendorDetailResponse getVendorDetail(Long vendorId);

    VendorPresignedUrlResponse generateDownloadUrl(Long vendorId);

}