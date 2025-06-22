package com.harusari.chainware.exception.auth;

import lombok.Getter;

@Getter
public class RefreshTokenMismatchException extends RuntimeException {

    private final AuthErrorCode errorCode;

    public RefreshTokenMismatchException(AuthErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}