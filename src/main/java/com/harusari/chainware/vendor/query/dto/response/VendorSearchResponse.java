package com.harusari.chainware.vendor.query.dto.response;

import com.harusari.chainware.common.domain.vo.Address;
import com.harusari.chainware.vendor.command.domain.aggregate.VendorStatus;
import com.harusari.chainware.vendor.command.domain.aggregate.VendorType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record VendorSearchResponse(
        Long vendorId, String vendorName, Address vendorAddress,
        String vendorMangerName, String phoneNumber, VendorType vendorType,
        VendorStatus vendorStatus, LocalDate vendorStartDate, LocalDate vendorEndDate
) {
}