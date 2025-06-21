package com.harusari.chainware.exception.auth;

import lombok.Getter;

@Getter
public class ExpiredJwtTokenException extends RuntimeException {

    private final AuthErrorCode errorCode;

    public ExpiredJwtTokenException(AuthErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}