package com.harusari.chainware.warehouse.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum WarehouseErrorCode {

    WAREHOUSE_NOT_FOUND("10001", "존재하지 않는 창고입니다.", HttpStatus.NOT_FOUND),
    INVALID_WAREHOUSE_STATUS("10002", "조작이 불가능한 상태의 창고입니다.", HttpStatus.BAD_REQUEST),
    INVENTORY_NOT_FOUND("10003", "재고가 없는 제품입니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    private static final Map<String, WarehouseErrorCode> MESSAGE_TO_ENUM;

    static {
        MESSAGE_TO_ENUM = Arrays.stream(values())
                .collect(Collectors.toMap(WarehouseErrorCode::getErrorMessage, e -> e));
    }

    public static Optional<WarehouseErrorCode> fromMessage(String message) {
        return Optional.ofNullable(MESSAGE_TO_ENUM.get(message));
    }

}