package com.harusari.chainware.vendor.command.domain.aggregate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VendorStatus {

    IN_PROGRESS("계약 진행 중"),
    TERMINATED("계약 만료");

    private final String vendorLabelKo;

}