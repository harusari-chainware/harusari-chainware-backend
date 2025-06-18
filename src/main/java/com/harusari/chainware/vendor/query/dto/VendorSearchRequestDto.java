package com.harusari.chainware.vendor.query.dto;

import com.harusari.chainware.vendor.command.domain.aggregate.VendorStatus;
import com.harusari.chainware.vendor.command.domain.aggregate.VendorType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VendorSearchRequestDto {

    private Integer page = 1;
    private Integer size = 10;

    private String vendorName;
    private String vendorAddress;
    private VendorType vendorType;
    private VendorStatus vendorStatus;
    private LocalDate baseDate;

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }
}
