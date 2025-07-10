package com.harusari.chainware.vendor.query.repository;

import com.harusari.chainware.vendor.query.dto.request.VendorSearchRequest;
import com.harusari.chainware.vendor.query.dto.response.VendorContractInfoResponse;
import com.harusari.chainware.vendor.query.dto.response.VendorDetailResponse;
import com.harusari.chainware.vendor.query.dto.response.VendorSearchResponse;
import com.harusari.chainware.vendor.query.dto.response.VendorSimpleResponse;
import com.harusari.chainware.warehouse.query.dto.response.WarehouseSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface VendorQueryRepositoryCustom {

    Page<VendorSearchResponse> pageVendors(VendorSearchRequest vendorSearchRequest, Pageable pageable);

    Optional<VendorDetailResponse> findVendorDetailByVendorId(Long vendorId);

    List<VendorSimpleResponse> findAllVendorsSimple();

    Optional<VendorContractInfoResponse> findVendorContractInfoByVendorName(String vendorName);

}