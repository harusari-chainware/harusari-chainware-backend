package com.harusari.chainware.purchase.command.domain.aggregate;

public enum PurchaseOrderStatus {
    REQUESTED,          // 요청
    CANCELLED,          // 요청 취소
    APPROVED,           // 승인
    REJECTED,           // 반려
    DELIVERED,          // 출고 완료
    RECEIVED          // 창고 입고
}
