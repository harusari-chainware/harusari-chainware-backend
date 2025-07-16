package com.harusari.chainware.exception.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CategoryErrorCode {

    /* top_category */
    TOP_CATEGORY_NOT_FOUND("20000", "상위 카테고리를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    TOP_CATEGORY_NAME_ALREADY_EXISTS("20001", "이미 존재하는 상위 카테고리 이름입니다.", HttpStatus.CONFLICT),
    TOP_CATEGORY_CANNOT_DELETE_HAS_PRODUCTS("20002","상위 카테고리에 연결된 카테고리가 있어 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST),

    /* category */
    CATEGORY_NOT_FOUND("21000", "해당 카테고리를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CATEGORY_NAME_ALREADY_EXISTS("21001", "이미 존재하는 카테고리 이름입니다.", HttpStatus.CONFLICT),
    CATEGORY_CANNOT_DELETE_HAS_PRODUCTS("21002", "카테고리에 연결된 제품이 있어 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST),


    ;

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

}
