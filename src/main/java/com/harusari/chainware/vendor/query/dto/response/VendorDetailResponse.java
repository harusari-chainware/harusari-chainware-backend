package com.harusari.chainware.vendor.query.dto.response;

import com.harusari.chainware.common.domain.vo.Address;
import com.harusari.chainware.vendor.command.domain.aggregate.VendorStatus;
import com.harusari.chainware.vendor.command.domain.aggregate.VendorType;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record VendorDetailResponse(
        Long vendorId, String vendorName, Address vendorAddress, Long memberId,
        String vendorManagerName, String phoneNumber, VendorType vendorType, String vendorTaxId,
        String vendorMemo, VendorStatus vendorStatus, String agreementOriginalFileName,
        Long agreementFileSize, LocalDate vendorStartDate, LocalDate vendorEndDate,
        LocalDateTime createdAt, LocalDateTime modifiedAt
) {
}