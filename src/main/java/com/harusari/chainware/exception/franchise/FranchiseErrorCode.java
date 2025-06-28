package com.harusari.chainware.exception.franchise;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FranchiseErrorCode {

    FRANCHISE_NOT_FOUND_EXCEPTION("20001", "존재하지 않은 가맹점입니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

}