package com.harusari.chainware.order.command.domain.aggregate;

public enum OrderStatus {
    REQUESTED,
    CANCELLED,
    APPROVED,
    REJECTED
}