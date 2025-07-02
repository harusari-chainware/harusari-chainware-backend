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
    PRODUCT_NOT_FOUND_FOR_INVENTORY("10002", "보유 재고에 등록할 제품 정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_INVENTORY_ALREADY_EXISTS("10003", "이미 보유 재고로 등록된 제품이 존재합니다.", HttpStatus.BAD_REQUEST),
    INVENTORY_NOT_FOUND("10004", "재고가 없는 제품입니다.", HttpStatus.BAD_REQUEST),
    INVENTORY_CANNOT_BE_DELETED_WHILE_QUANTITY_EXISTS("20014","현재 보유 재고 또는 예약 재고가 남아 있어 재고를 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST),
    QUANTITY_LESS_THAN_SAFETY_STOCK("10005", "보유 재고는 안전 재고보다 작을 수 없습니다.", HttpStatus.BAD_REQUEST ),
    QUANTITY_LESS_THAN_RESERVED_STOCK("10006", "보유 재고는 예약 재고보다 작을 수 없습니다.", HttpStatus.BAD_REQUEST);

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