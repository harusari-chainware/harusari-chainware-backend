package com.harusari.chainware.delivery.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum DeliveryErrorCode {

    DELIVERY_NOT_FOUND("10001", "존재하지 않는 배송입니다.", HttpStatus.BAD_REQUEST),
    INVALID_DELIVERY_STATUS("10002", "상태 변경이 불가능한 배송입니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    private static final Map<String, DeliveryErrorCode> MESSAGE_TO_ENUM;

    static {
        MESSAGE_TO_ENUM = Arrays.stream(values())
                .collect(Collectors.toMap(DeliveryErrorCode::getErrorMessage, e -> e));
    }

    public static Optional<DeliveryErrorCode> fromMessage(String message) {
        return Optional.ofNullable(MESSAGE_TO_ENUM.get(message));
    }

}