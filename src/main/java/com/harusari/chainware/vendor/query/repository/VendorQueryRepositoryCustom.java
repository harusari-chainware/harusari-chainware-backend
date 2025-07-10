package com.harusari.chainware.vendor.query.repository;

import com.harusari.chainware.vendor.query.dto.request.VendorSearchRequest;
import com.harusari.chainware.vendor.query.dto.response.VendorContractInfoResponse;
import com.harusari.chainware.vendor.query.dto.response.VendorDetailResponse;
import com.harusari.chainware.vendor.query.dto.response.VendorSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface VendorQueryRepositoryCustom {

    Page<VendorSearchResponse> pageVendors(VendorSearchRequest vendorSearchRequest, Pageable pageable);

    Optional<VendorDetailResponse> findVendorDetailByVendorId(Long vendorId);

    Optional<VendorContractInfoResponse> findVendorContractInfoByVendorName(String vendorName);

}