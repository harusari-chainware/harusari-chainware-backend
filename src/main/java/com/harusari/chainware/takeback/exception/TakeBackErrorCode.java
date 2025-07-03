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
    INVALID_TAKE_BACK_STATUS_FOR_CANCEL("10002", "신청 상태가 아닌 반품은 취소할 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_TAKE_BACK_STATUS_FOR_COLLECT("10003", "신청 상태가 아닌 반품은 수거할 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_TAKE_BACK_STATUS_FOR_APPROVE("10004", "수거 상태가 아닌 반품은 승인할 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_TAKE_BACK_STATUS_FOR_REJECT("10005", "수거 상태가 아닌 반품은 반려할 수 없습니다.", HttpStatus.BAD_REQUEST),
    ORDER_DETAIL_NOT_FOUND("10006", "반품할 수 있는 주문 상세 제품이 아닙니다.", HttpStatus.BAD_REQUEST),
    WAREHOUSE_NOT_FOUND("10007", "해당 사용자가 관리하는 창고가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    INVALID_TAKE_BACK_ITEMS("10008", "반품 항목 정보가 없습니다.", HttpStatus.BAD_REQUEST),
    IMAGE_COUNT_MISMATCH("10009", "반품 이미지 개수가 반품 항목 개수와 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    IMAGE_FILE_NOT_FOUND("10010", "반품 이미지가 누락되었습니다.", HttpStatus.BAD_REQUEST),
    INVENTORY_NOT_FOUND("10011", "재고가 없는 제품입니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ACCESS_TO_ORDER("10012", "해당 주문에 접근할 수 있는 권한이 없습니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ACCESS_TO_WAREHOUSE("10013", "해당 창고에 접근할 수 있는 권한이 없습니다.", HttpStatus.BAD_REQUEST);

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