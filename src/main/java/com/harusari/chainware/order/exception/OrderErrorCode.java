package com.harusari.chainware.order.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode {

    INVENTORY_LOCK_TIMEOUT("10001", "재고 확인 중 락 획득에 실패했습니다.", HttpStatus.CONFLICT),
    EMPTY_ORDER_DETAIL("10002", "주문 상세 항목이 비어 있습니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_INVENTORY_NOT_FOUND("10003", "해당 제품의 창고 재고를 찾을 수 없습니.다.", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_QUANTITY("10004", "주문 수량은 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_AVAILABLE_QUANTITY("10005", "가용 재고를 초과하는 수량은 주문할 수 없습니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND("10006", "해당 제품이 없습니다.", HttpStatus.NOT_FOUND),
    ORDER_SAVE_FAILED("10007", "주문 저장 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ORDER_NOT_FOUND("10008", "주문 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CANNOT_UPDATE_ORDER_STATUS("10009", "해당 주문은 현재 상태에서는 수정할 수 없습니다.", HttpStatus.BAD_REQUEST),
    CANNOT_CANCEL_ORDER("10010", "해당 주문은 현재 상태에서는 취소할 수 없습니다.", HttpStatus.BAD_REQUEST),
    CANNOT_APPROVE_ORDER("10011", "해당 주문은 현재 상태에서는 승인할 수 없습니다.", HttpStatus.BAD_REQUEST),
    CANNOT_REJECT_ORDER("10012", "해당 주문은 현재 상태에서는 반려할 수 없습니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ORDER_ACCESS("10013", "해당 주문에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),
    REJECT_REASON_REQUIRED("10014", "반려 사유를 입력해야 합니다.", HttpStatus.BAD_REQUEST),
    FRANCHISE_NOT_FOUND_FOR_MANAGER("13005", "해당 관리자가 등록된 가맹점이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    ;

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    private static final Map<String, OrderErrorCode> MESSAGE_TO_ENUM;

    static {
        MESSAGE_TO_ENUM = Arrays.stream(values())
                .collect(Collectors.toMap(OrderErrorCode::getErrorMessage, e -> e));
    }

    public static Optional<OrderErrorCode> fromMessage(String message) {
        return Optional.ofNullable(MESSAGE_TO_ENUM.get(message));
    }

}