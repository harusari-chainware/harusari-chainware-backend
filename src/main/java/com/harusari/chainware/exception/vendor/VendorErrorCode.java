package com.harusari.chainware.exception.vendor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum VendorErrorCode {

    VENDOR_NOT_FOUND_EXCEPTION("50001", "존재하지 않는 거래처 입니다.", HttpStatus.BAD_REQUEST),
    VENDOR_AGREEMENT_NOT_FOUND("50002", "해당 거래처에 등록된 계약서가 없습니다.", HttpStatus.BAD_REQUEST),;

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

}