package com.harusari.chainware.exception.member.handler;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.exception.member.*;
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

    @ExceptionHandler(InvalidCurrentPasswordException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidCurrentPasswordException(InvalidCurrentPasswordException e) {
        MemberErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(InvalidPasswordChangeException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidPasswordChangeException(InvalidPasswordChangeException e) {
        MemberErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(PasswordConfirmationMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handlePasswordConfirmationMismatchException(PasswordConfirmationMismatchException e) {
        MemberErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(PasswordSameAsCurrentException.class)
    public ResponseEntity<ApiResponse<Void>> handlePasswordSameAsCurrentException(PasswordSameAsCurrentException e) {
        MemberErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getErrorCode(), errorCode.getErrorMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

}