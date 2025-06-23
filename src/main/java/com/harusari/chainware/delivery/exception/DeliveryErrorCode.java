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
    DELIVERY_STATUS_NOT_REQUESTED("10002", "수정 불가능한 상태의 배송입니다.", HttpStatus.BAD_REQUEST),
    ORDER_DETAIL_NOT_FOUND_FOR_DELIVERY("10003", "배송할 주문 상세 정보가 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_INVENTORY_NOT_FOUND_FOR_DELIVERY("10004", "배송할 제품의 재고 정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_INVENTORY_FOR_DELIVERY("10005", "배송할 제품의 수량이 충분하지 않습니다.", HttpStatus.BAD_REQUEST),
    DELIVERY_STATUS_NOT_IN_TRANSIT("10006", "완료 처리가 불가능한 상태의 배송입니다.", HttpStatus.BAD_REQUEST);

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