package com.harusari.chainware.exception.member;

import lombok.Getter;

@Getter
public class EmailAlreadyExistsException extends RuntimeException{

    private final MemberErrorCode errorCode;

    public EmailAlreadyExistsException(MemberErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}