package com.harusari.chainware.exception.member;

import lombok.Getter;

@Getter
public class InvalidCurrentPasswordException extends RuntimeException {

    private final MemberErrorCode errorCode;

    public InvalidCurrentPasswordException(MemberErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}