package com.harusari.chainware.exception.purchase;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PurchaseOrderErrorCode {

    PURCHASE_NOT_FOUND("PO001", "해당 발주를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PURCHASE_APPROVE_INVALID_STATUS("PO002", "이미 처리 된 발주입니다.", HttpStatus.BAD_REQUEST),
    PURCHASE_REJECT_INVALID_STATUS("PO003", "현재 상태에서는 해당 발주를 반려할 수 없습니다.", HttpStatus.BAD_REQUEST),
    PURCHASE_CANCEL_INVALID_STATUS("PO004", "현재 상태에서는 해당 발주 취소할 수 없습니다.", HttpStatus.BAD_REQUEST),
    PURCHASE_UNAUTHORIZED_VENDOR("PO005", "해당 발주의 거래처 담당자가 아닙니다.", HttpStatus.FORBIDDEN),
    PURCHASE_UNAUTHORIZED_WRITER("PO006", "해당 요청을 수행할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    PURCHASE_UPDATE_INVALID_STATUS("PO020", "요청 상태일 때만 수정할 수 있습니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;
}
