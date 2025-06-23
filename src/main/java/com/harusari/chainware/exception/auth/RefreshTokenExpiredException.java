package com.harusari.chainware.exception.auth;

import lombok.Getter;

@Getter
public class RefreshTokenExpiredException extends RuntimeException {

    private final AuthErrorCode errorCode;

    public RefreshTokenExpiredException(AuthErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}