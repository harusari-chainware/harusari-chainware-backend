package com.harusari.chainware.exception.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode {

    EMAIL_VERIFICATION_REQUIRED("10001", "이메일 중복 확인을 먼저 해주세요.", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS("10002", "이미 존재하는 이메일입니다.", HttpStatus.BAD_REQUEST),
    INVALID_MEMBER_AUTHORITY("10003", "회원 권한이 올바르지 않습니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

}