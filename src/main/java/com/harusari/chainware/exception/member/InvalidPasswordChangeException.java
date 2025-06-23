package com.harusari.chainware.exception.member;

import lombok.Getter;

@Getter
public class InvalidPasswordChangeException extends RuntimeException {

    private final MemberErrorCode errorCode;

    public InvalidPasswordChangeException(MemberErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}