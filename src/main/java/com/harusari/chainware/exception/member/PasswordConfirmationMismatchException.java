package com.harusari.chainware.exception.member;

import lombok.Getter;

@Getter
public class PasswordConfirmationMismatchException extends RuntimeException {

    private final MemberErrorCode errorCode;

    public PasswordConfirmationMismatchException(MemberErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}