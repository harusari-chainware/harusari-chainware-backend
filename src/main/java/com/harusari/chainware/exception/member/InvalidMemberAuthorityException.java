package com.harusari.chainware.exception.member;

import lombok.Getter;

@Getter
public class InvalidMemberAuthorityException extends RuntimeException {

    private final MemberErrorCode errorCode;

    public InvalidMemberAuthorityException(MemberErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}