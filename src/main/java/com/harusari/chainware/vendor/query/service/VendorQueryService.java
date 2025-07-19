package com.harusari.chainware.vendor.query.service;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.vendor.query.dto.response.VendorContractInfoResponse;
import com.harusari.chainware.vendor.query.dto.response.VendorPresignedUrlResponse;
import com.harusari.chainware.vendor.query.dto.response.VendorDetailResponse;
import com.harusari.chainware.vendor.query.dto.request.VendorSearchRequest;
import com.harusari.chainware.vendor.query.dto.response.VendorSearchResponse;
import com.harusari.chainware.vendor.query.dto.response.VendorSimpleResponse;
import com.harusari.chainware.warehouse.query.dto.response.WarehouseSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VendorQueryService {

    Page<VendorSearchResponse> searchVendors(VendorSearchRequest vendorSearchRequest, Pageable pageable);

    VendorDetailResponse getVendorDetail(Long vendorId);

    VendorPresignedUrlResponse generateDownloadUrl(Long vendorId);

    List<VendorSimpleResponse> getAllVendors();

    VendorContractInfoResponse getVendorContractInfo(String vendorName);

    Page<VendorSearchResponse> searchVendors(CustomUserDetails userDetails, VendorSearchRequest vendorSearchRequest, Pageable pageable);

}