package com.harusari.chainware.vendor.command.application.dto;

import com.harusari.chainware.vendor.command.domain.aggregate.VendorStatus;
import com.harusari.chainware.vendor.command.domain.aggregate.VendorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record VendorUpdateRequestDto (
        @NotBlank String vendorName, @NotNull VendorType vendorType,
        @NotBlank String vendorAddress, @NotBlank String vendorTaxId,
        @NotBlank String vendorMemo, @NotNull VendorStatus vendorStatus,
        @NotBlank String agreement, @NotNull LocalDate vendorStartDate,
        @NotNull LocalDate vendorEndDate
) {
}