package com.harusari.chainware.exception.member.handler;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.exception.member.EmailAlreadyExistsException;
import com.harusari.chainware.exception.member.EmailVerificationRequiredException;
import com.harusari.chainware.exception.member.InvalidMemberAuthorityException;
import com.harusari.chainware.exception.member.MemberErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(EmailVerificationRequiredException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailVerificationRequiredException(EmailVerificationRequiredException e) {
        MemberErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        MemberErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(InvalidMemberAuthorityException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidMemberAuthorityException(InvalidMemberAuthorityException e) {
        MemberErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

}