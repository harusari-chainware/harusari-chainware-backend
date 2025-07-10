package com.harusari.chainware.vendor.query.dto.response;

import com.harusari.chainware.vendor.command.domain.aggregate.VendorStatus;
import com.harusari.chainware.vendor.command.domain.aggregate.VendorType;
import lombok.Builder;

@Builder
public record VendorContractInfoResponse(
        Long vendorId, VendorType vendorType, String vendorTaxId, VendorStatus vendorStatus
) {
}