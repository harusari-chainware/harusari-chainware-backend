package com.harusari.chainware.exception.auth;

import lombok.Getter;

@Getter
public class RefreshTokenNotFoundException extends RuntimeException {

    private final AuthErrorCode errorCode;

    public RefreshTokenNotFoundException(AuthErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}