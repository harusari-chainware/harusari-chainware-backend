package com.harusari.chainware.exception.auth;

import lombok.Getter;

@Getter
public class JwtTokenEmptyException extends RuntimeException {

    private final AuthErrorCode errorCode;

    public JwtTokenEmptyException(AuthErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}