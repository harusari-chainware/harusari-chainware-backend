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
    PURCHASE_UPDATE_INVALID_STATUS("PO007", "요청 상태일 때만 수정할 수 있습니다.", HttpStatus.BAD_REQUEST),
    PURCHASE_SHIP_INVALID_STATUS("PO008", "아직 승인되지 않은 발주입니다. 출고 처리 할 수 없습니다.", HttpStatus.BAD_REQUEST),
    PURCHASE_INBOUND_INVALID_STATUS("PO009", "아직 출고 되지 않은 발주입니다. 입고 처리 할 수 없습니다.", HttpStatus.BAD_REQUEST),
    NO_PURCHASE_ORDER_DETAILS("PO010", "발주 품목을 찾을 수 없습니다. 내용을 확인해주세요.", HttpStatus.BAD_REQUEST),
    EXPIRATION_DATE_REQUIRED("PO011", "유통기한을 반드시 입력해야 합니다.", HttpStatus.BAD_REQUEST);


    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;
}
