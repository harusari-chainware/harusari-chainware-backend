package com.harusari.chainware.takeback.command.domain.aggregate;

public enum TakeBackStatus {
    REQUESTED,
    CANCELLED,
    COLLECTED,
    APPROVED,
    REJECTED
}
