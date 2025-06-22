package com.harusari.chainware.exception.auth;

import lombok.Getter;

@Getter
public class InvalidJwtTokenException extends RuntimeException {

    private final AuthErrorCode errorCode;

    public InvalidJwtTokenException(AuthErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}