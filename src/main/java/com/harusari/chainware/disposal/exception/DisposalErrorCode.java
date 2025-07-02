package com.harusari.chainware.disposal.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DisposalErrorCode {

    FRANCHISE_NOT_FOUND("60001", "해당 가맹점 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    WAREHOUSE_NOT_FOUND("60002", "해당 창고 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NO_DISPOSAL_AUTHORITY("60003", "폐기를 등록할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    INVENTORY_NOT_FOUND("60004", "해당 창고의 재고 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INSUFFICIENT_INVENTORY("60005", "재고가 부족하여 폐기할 수 없습니다.", HttpStatus.BAD_REQUEST),
    DISPOSAL_QUERY_NO_FRANCHISE("60006", "가맹점 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    DISPOSAL_QUERY_NO_WAREHOUSE("60007", "창고 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    DISPOSAL_QUERY_UNAUTHORIZED("60008", "폐기 조회 권한이 없습니다.", HttpStatus.FORBIDDEN);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    public String getMessage() {
        return this.errorMessage;
    }
}
