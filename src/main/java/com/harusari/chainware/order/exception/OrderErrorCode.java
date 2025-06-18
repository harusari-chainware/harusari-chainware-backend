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