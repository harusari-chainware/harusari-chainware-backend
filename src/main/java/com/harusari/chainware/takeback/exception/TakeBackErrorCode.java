package com.harusari.chainware.takeback.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum TakeBackErrorCode {

    TAKE_BACK_NOT_FOUND("10001", "존재하지 않는 반품입니다.", HttpStatus.NOT_FOUND),
    INVALID_TAKE_BACK_STATUS_FOR_CANCEL("10002", "조작이 불가능한 상태의 반품입니다.", HttpStatus.BAD_REQUEST),
    INVENTORY_NOT_FOUND("10003", "재고가 없는 제품입니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    private static final Map<String, TakeBackErrorCode> MESSAGE_TO_ENUM;

    static {
        MESSAGE_TO_ENUM = Arrays.stream(values())
                .collect(Collectors.toMap(TakeBackErrorCode::getErrorMessage, e -> e));
    }

    public static Optional<TakeBackErrorCode> fromMessage(String message) {
        return Optional.ofNullable(MESSAGE_TO_ENUM.get(message));
    }

}