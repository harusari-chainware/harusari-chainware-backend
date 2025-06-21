package com.harusari.chainware.exception.auth;

import lombok.Getter;

@Getter
public class EmptyJwtClaimsException extends RuntimeException {

    private final AuthErrorCode errorCode;

    public EmptyJwtClaimsException(AuthErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}