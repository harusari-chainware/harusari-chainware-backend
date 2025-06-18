package com.harusari.chainware.vendor.query.dto;

import com.harusari.chainware.vendor.command.domain.aggregate.VendorStatus;
import com.harusari.chainware.vendor.command.domain.aggregate.VendorType;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class VendorListDto {

    private Long vendorId;
    private String vendorName;
    private VendorType vendorType;
    private String vendorAddress;
    private VendorStatus vendorStatus;
    private LocalDate vendorEndDate;
}
