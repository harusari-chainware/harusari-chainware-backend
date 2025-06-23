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

    ORDER_UPDATE_INVALID("10001", "수정 가능한 상태가 아닙니다.", HttpStatus.BAD_REQUEST),
    REJECT_REASON_REQUIRED("10002", "반려 사유가 필요합니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_INVENTORY_NOT_FOUND("10003", "창고에 보유 재고가 없는 제품입니다.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_AVAILABLE_QUANTITY("10004", "주문 가능 재고를 초과하는 주문은 불가능합니다.", HttpStatus.BAD_REQUEST),
    MEMBER_NOT_FOUND("10005", "존재하지 않는 사용자입니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ORDER_ACCESS("10006", "조작이 불가능한 권한의 사용자입니다.", HttpStatus.BAD_REQUEST),
    METHOD_ARG_NOT_VALID("10999", "@Valid 검증 오류입니다.", HttpStatus.BAD_REQUEST);

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