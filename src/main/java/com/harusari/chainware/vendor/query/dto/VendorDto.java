package com.harusari.chainware.vendor.query.dto;

import com.harusari.chainware.vendor.command.domain.aggregate.VendorStatus;
import com.harusari.chainware.vendor.command.domain.aggregate.VendorType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VendorDto {
    private Long vendorId;
    private String vendorName;
    private VendorType vendorType;
    private String vendorTaxId;
    private VendorStatus vendorStatus;
}
