package com.harusari.chainware.order.command.application.dto.request;

import lombok.Getter;

@Getter
public class OrderRejectRequest {
    private String rejectReason;
}

