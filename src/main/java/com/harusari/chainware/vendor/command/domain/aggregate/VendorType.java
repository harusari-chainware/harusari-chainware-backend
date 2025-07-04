package com.harusari.chainware.vendor.command.domain.aggregate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VendorType {

    SUPPLIER("공급업체"),
    TRUST_CONTRACTOR("위탁업체"),
    LOGISTICS("물류"),
    AGENCY("대행업체");

    private final String vendorTypeLabelKo;

}