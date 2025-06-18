package com.harusari.chainware.exception.member;

import lombok.Getter;

@Getter
public class EmailVerificationRequiredException extends RuntimeException{

    private final MemberErrorCode errorCode;

    public EmailVerificationRequiredException(MemberErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}