package com.harusari.chainware.order.command.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderApproveRequest {
    private Long warehouseId;
}