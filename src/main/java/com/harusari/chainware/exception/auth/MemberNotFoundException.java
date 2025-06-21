package com.harusari.chainware.exception.auth;

import lombok.Getter;

@Getter
public class MemberNotFoundException extends RuntimeException {

    private final AuthErrorCode errorCode;

    public MemberNotFoundException(AuthErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}