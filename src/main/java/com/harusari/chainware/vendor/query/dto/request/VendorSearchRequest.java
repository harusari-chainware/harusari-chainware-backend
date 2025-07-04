package com.harusari.chainware.vendor.query.dto.request;

import com.harusari.chainware.vendor.command.domain.aggregate.VendorStatus;
import com.harusari.chainware.vendor.command.domain.aggregate.VendorType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record VendorSearchRequest(
        String vendorName, String zipcode, String addressRoad,
        String addressDetail, VendorType vendorType, VendorStatus vendorStatus,
        LocalDate contractStartDate, LocalDate contractEndDate
) {
}