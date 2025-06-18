package com.harusari.chainware.member.command.application.dto.request.warehouse;

import lombok.Builder;

@Builder
public record WarehouseCreateRequest(
        String warehouseName, String warehouseAddress
) {
}