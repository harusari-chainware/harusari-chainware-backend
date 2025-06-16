package com.harusari.chainware.vendor.query.controller;

import com.harusari.chainware.vendor.query.dto.VendorDetailResponse;
import com.harusari.chainware.vendor.query.dto.VendorListResponse;
import com.harusari.chainware.vendor.query.dto.VendorSearchRequestDto;
import com.harusari.chainware.vendor.query.service.VendorQueryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
public class VendorQueryController {

    private final VendorQueryServiceImpl vendorQueryService;

    @GetMapping
    public VendorListResponse getVendors(VendorSearchRequestDto request) {
        return vendorQueryService.getVendors(request);
    }

    @GetMapping("/{vendorId}")
    public VendorDetailResponse findVendorDetail(@PathVariable Long vendorId) {
        return vendorQueryService.getVendorDetail(vendorId);
    }
}
