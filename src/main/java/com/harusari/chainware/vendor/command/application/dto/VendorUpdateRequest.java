package com.harusari.chainware.vendor.command.application.dto;

import com.harusari.chainware.common.dto.AddressRequest;
import com.harusari.chainware.vendor.command.domain.aggregate.VendorStatus;
import com.harusari.chainware.vendor.command.domain.aggregate.VendorType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record VendorUpdateRequest(
        String vendorName, VendorType vendorType, String vendorTaxId,
        AddressRequest addressRequest, String vendorMemo, VendorStatus vendorStatus,
        LocalDate vendorStartDate, LocalDate vendorEndDate
) {
}