package com.harusari.chainware.exception.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductErrorCode {

    PRODUCT_NOT_FOUND("30001", "해당 상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    PRODUCT_ALREADY_EXISTS("30002", "이미 존재하는 상품입니다.", HttpStatus.CONFLICT),

    INVALID_PRODUCT_STATUS("30003", "유효하지 않은 상품 상태입니다.", HttpStatus.BAD_REQUEST),

    PRODUCT_CANNOT_BE_DELETED("30004", "삭제할 수 없는 상품입니다.", HttpStatus.BAD_REQUEST),

    PRODUCT_UPDATE_FAILED("30005", "상품 정보 수정에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    PRODUCT_ALREADY_DELETED("30006", "이미 삭제된 상품입니다.", HttpStatus.BAD_REQUEST),

    ;

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

}
