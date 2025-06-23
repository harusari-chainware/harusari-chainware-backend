package com.harusari.chainware.exception.auth;

import lombok.Getter;

@Getter
public class UnsupportedJwtTokenException extends RuntimeException {

    private final AuthErrorCode errorCode;

    public UnsupportedJwtTokenException(AuthErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}