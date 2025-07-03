package com.harusari.chainware.vendor.query.service;

import com.harusari.chainware.common.dto.Pagination;
import com.harusari.chainware.vendor.query.dto.*;
import com.harusari.chainware.vendor.query.mapper.VendorQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorQueryServiceImpl implements VendorQueryService {

    private final VendorQueryMapper vendorMapper;

    @Override
    @Transactional(readOnly = true)
    public VendorListResponse getVendors(VendorSearchRequestDto request) {
        List<VendorListDto> vendors = vendorMapper.findVendors(request);
        long totalCount = vendorMapper.countVendors(request);
        int totalPages = (int) Math.ceil((double) totalCount / request.getSize());

        return VendorListResponse.builder()
                .vendors(vendors)
                .pagination(Pagination.builder()
                        .currentPage(request.getPage())
                        .totalPages(totalPages)
                        .totalItems(totalCount)
                        .build())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public VendorDetailResponse getVendorDetail(Long vendorId) {
        VendorDetailDto vendorDetail = vendorMapper.findVendorDetailById(vendorId);
        return VendorDetailResponse.builder()
                .vendor(vendorDetail)
                .build();
    }
}
