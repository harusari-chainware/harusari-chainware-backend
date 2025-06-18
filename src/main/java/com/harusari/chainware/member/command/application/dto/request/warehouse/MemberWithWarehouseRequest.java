package com.harusari.chainware.member.command.application.dto.request.warehouse;

import com.harusari.chainware.member.command.application.dto.request.MemberCreateRequest;
import lombok.Builder;

@Builder
public record MemberWithWarehouseRequest(
        MemberCreateRequest memberCreateRequest,
        WarehouseCreateRequest warehouseCreateRequest
) {
}