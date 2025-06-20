package com.harusari.chainware.exception.contract;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ContractErrorCode {

    CONTRACT_NOT_FOUND("50000", "계약 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CONTRACT_ALREADY_EXISTS("50001", "이미 존재하는 계약입니다.", HttpStatus.CONFLICT),
    CONTRACT_CANNOT_DELETE("50002", "계약을 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST),
    CONTRACT_PERIOD_INVALID("50003", "계약 시작일은 종료일보다 앞서야 합니다.", HttpStatus.BAD_REQUEST),
    CONTRACT_VENDOR_PRODUCT_CONFLICT("50004", "해당 제품과 거래처 간 계약이 이미 존재합니다.", HttpStatus.CONFLICT),

    ;

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;
}