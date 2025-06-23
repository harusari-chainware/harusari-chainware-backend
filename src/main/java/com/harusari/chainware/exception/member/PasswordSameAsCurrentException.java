package com.harusari.chainware.exception.member;

import lombok.Getter;

@Getter
public class PasswordSameAsCurrentException extends RuntimeException {

    private final MemberErrorCode errorCode;

    public PasswordSameAsCurrentException(MemberErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}