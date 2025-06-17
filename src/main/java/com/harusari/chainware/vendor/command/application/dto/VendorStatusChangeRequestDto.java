package com.harusari.chainware.vendor.command.application.dto;

import com.harusari.chainware.vendor.command.domain.aggregate.VendorStatus;
import jakarta.validation.constraints.NotNull;

public record VendorStatusChangeRequestDto(
        @NotNull VendorStatus vendorStatus
) {}
