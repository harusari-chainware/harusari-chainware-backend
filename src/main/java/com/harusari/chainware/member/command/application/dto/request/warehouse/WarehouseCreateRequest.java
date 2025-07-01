package com.harusari.chainware.member.command.application.dto.request.warehouse;

import com.harusari.chainware.common.dto.AddressRequest;
import lombok.Builder;

@Builder
public record WarehouseCreateRequest(
        String warehouseName, AddressRequest addressRequest
) {
}