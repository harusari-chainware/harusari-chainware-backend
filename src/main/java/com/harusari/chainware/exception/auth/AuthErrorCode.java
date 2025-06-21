package com.harusari.chainware.exception.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode {

    MEMBER_NOT_FOUND_EXCEPTION("12001", "존재하지 않은 회원입니다.", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS_EXCEPTION("12002", "올바르지 않은 아이디 혹은 비밀번호 입니다.", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_NOT_FOUND_EXCEPTION("12003", "RefreshToken이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_MISMATCH_EXCEPTION("12004", "RefreshToken이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_EXPIRED_EXCEPTION("12005", "RefreshToken이 유효시간이 만료되었습니다.", HttpStatus.BAD_REQUEST),
    JWT_TOKEN_EMPTY_EXCEPTION("12006", "토큰이 비어있습니다.", HttpStatus.BAD_REQUEST),
    INVALID_JWT_TOKEN_EXCEPTION("12007", "유효하지 않은 JWT 토큰입니다.", HttpStatus.BAD_REQUEST),
    EXPIRED_JWT_TOKEN_EXCEPTION("12008", "만료된 JWT 토큰입니다.", HttpStatus.BAD_REQUEST),
    UNSUPPORTED_JWT_TOKEN_EXCEPTION("12009", "지원하지 않는 JWT 토큰입니다.", HttpStatus.BAD_REQUEST),
    EMPTY_JWT_CLAIMS_EXCEPTION("12010", "JWT 클레임이 비어 있습니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

}