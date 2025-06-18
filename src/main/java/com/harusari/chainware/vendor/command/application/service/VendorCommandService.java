package com.harusari.chainware.vendor.command.application.service;

import com.harusari.chainware.vendor.command.application.dto.VendorCreateRequestDto;
import com.harusari.chainware.vendor.command.application.dto.VendorStatusChangeRequestDto;
import com.harusari.chainware.vendor.command.application.dto.VendorUpdateRequestDto;

public interface VendorCommandService {
    Long createVendor(VendorCreateRequestDto requestDto);
    void updateVendor(Long vendorId, VendorUpdateRequestDto dto);
    void changeVendorStatus(Long vendorId, VendorStatusChangeRequestDto dto);
}
