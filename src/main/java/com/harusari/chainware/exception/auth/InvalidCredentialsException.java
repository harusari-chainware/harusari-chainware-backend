package com.harusari.chainware.exception.auth;

import lombok.Getter;

@Getter
public class InvalidCredentialsException extends RuntimeException {

    private final AuthErrorCode errorCode;

    public InvalidCredentialsException(AuthErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}