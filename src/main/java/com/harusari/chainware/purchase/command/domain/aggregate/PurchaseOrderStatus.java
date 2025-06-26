package com.harusari.chainware.purchase.command.domain.aggregate;

public enum PurchaseOrderStatus {
    REQUESTED,          // 요청
    CANCELLED,          // 요청 취소
    APPROVED,           // 승인
    REJECTED,           // 반려
    SHIPPED,          // 출고 완료
    WAREHOUSED          // 창고 입고
}
